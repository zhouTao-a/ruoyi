package org.dromara.mes.msg.service.impl;

import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.msg.support.MsgMaintainerHelper;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.mes.msg.domain.vo.MsgMatterGroupVo;
import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.msg.service.IMsgMatterGroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * 事件组Service业务层处理
 *
 * @author allen
 * @date 2025-06-18
 */
@RequiredArgsConstructor
@Service
public class MsgMatterGroupServiceImpl implements IMsgMatterGroupService {

    private final MsgMatterGroupMapper baseMapper;

    /**
     * 查询事件组
     *
     * @param id 主键
     * @return 事件组
     */
    @Override
    public MsgMatterGroupVo queryById(Long id){
        MsgMatterGroup exist = baseMapper.selectById(id);
        if (exist == null || !MsgMaintainerHelper.isOwner(exist.getCreateBy())) {
            return null;
        }
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询事件组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件组分页列表
     */
    @Override
    public TableDataInfo<MsgMatterGroupVo> queryPageList(MsgMatterGroupBo bo, PageQuery pageQuery) {
        MsgMaintainerHelper.apply(bo);
        Page<MsgMatterGroupVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的事件组列表
     *
     * @param bo 查询条件
     * @return 事件组列表
     */
    @Override
    public List<MsgMatterGroupVo> queryList(MsgMatterGroupBo bo) {
        MsgMaintainerHelper.apply(bo);
        Page<MsgMatterGroupVo> result = baseMapper.queryPageList(new PageQuery().build(), bo);
        return result.getRecords();
    }

    /**
     * 新增事件组
     *
     * @param bo 事件组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgMatterGroupBo bo) {
        List<String> groupIdList = bo.getGroupIdList();
        List<String> dayMatterIdList = bo.getDayMatterIdList();
        List<MsgMatterGroup> list = new ArrayList<>();
        if (!groupIdList.isEmpty() && !dayMatterIdList.isEmpty()) {
            for (String groupId : groupIdList) {
                for (String dayMatterId : dayMatterIdList) {
                    MsgMatterGroup msgMatterGroup = new MsgMatterGroup();
                    msgMatterGroup.setGroupId(Long.valueOf(groupId));
                    msgMatterGroup.setDayMatterId(Long.valueOf(dayMatterId));
                    if (validEntityBeforeSave(msgMatterGroup)) {
                        list.add(msgMatterGroup);
                    }
                }
            }
        }
        return list.isEmpty() || baseMapper.insertBatch(list);
    }

    /**
     * 修改事件组
     *
     * @param bo 事件组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgMatterGroupBo bo) {
        MsgMatterGroup exist = baseMapper.selectById(bo.getId());
        if (exist == null || !MsgMaintainerHelper.isOwner(exist.getCreateBy())) {
            throw new ServiceException("数据不存在");
        }
        List<String> groupIdList = bo.getGroupIdList();
        List<String> dayMatterIdList = bo.getDayMatterIdList();
        if (!groupIdList.isEmpty() && !dayMatterIdList.isEmpty()) {
            for (String groupId : groupIdList) {
                for (String dayMatterId : dayMatterIdList) {
                    MsgMatterGroup msgMatterGroup = new MsgMatterGroup();
                    msgMatterGroup.setGroupId(Long.valueOf(groupId));
                    msgMatterGroup.setDayMatterId(Long.valueOf(dayMatterId));
                    msgMatterGroup.setId(bo.getId());
                    if (validEntityBeforeSave(msgMatterGroup)) {
                        baseMapper.updateById(msgMatterGroup);
                    } else {
                        throw new ServiceException("事件组不能重复");
                    }
                }
            }
        }
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private boolean validEntityBeforeSave(MsgMatterGroup entity){
        return !baseMapper.exists(new LambdaQueryWrapper<MsgMatterGroup>()
            .eq(MsgMatterGroup::getGroupId, entity.getGroupId())
            .eq(MsgMatterGroup::getDayMatterId, entity.getDayMatterId())
            .eq(MsgMatterGroup::getCreateBy, MsgMaintainerHelper.currentUserId())
            .ne(entity.getId() != null, MsgMatterGroup::getId, entity.getId()));
    }

    /**
     * 校验并批量删除事件组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.delete(new LambdaQueryWrapper<MsgMatterGroup>()
            .in(MsgMatterGroup::getId, ids)
            .eq(MsgMatterGroup::getCreateBy, MsgMaintainerHelper.currentUserId())) > 0;
    }
}
