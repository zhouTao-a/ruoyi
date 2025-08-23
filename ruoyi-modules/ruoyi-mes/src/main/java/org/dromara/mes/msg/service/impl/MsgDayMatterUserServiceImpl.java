package org.dromara.mes.msg.service.impl;

import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterUserVo;
import org.dromara.mes.msg.domain.MsgDayMatterUser;
import org.dromara.mes.msg.mapper.MsgDayMatterUserMapper;
import org.dromara.mes.msg.service.IMsgDayMatterUserService;

import java.util.List;
import java.util.Collection;

/**
 * 事件与用户关联Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgDayMatterUserServiceImpl implements IMsgDayMatterUserService {

    private final MsgDayMatterUserMapper baseMapper;

    /**
     * 查询事件与用户关联
     *
     * @param id 主键
     * @return 事件与用户关联
     */
    @Override
    public MsgDayMatterUserVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询事件与用户关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件与用户关联分页列表
     */
    @Override
    public TableDataInfo<MsgDayMatterUserVo> queryPageList(MsgDayMatterUserBo bo, PageQuery pageQuery) {
        Page<MsgDayMatterUserVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的事件与用户关联列表
     *
     * @param bo 查询条件
     * @return 事件与用户关联列表
     */
    @Override
    public List<MsgDayMatterUserVo> queryList(MsgDayMatterUserBo bo) {
        Page<MsgDayMatterUserVo> result = baseMapper.queryPageList(new PageQuery().build(), bo);
        return result.getRecords();
    }

    /**
     * 新增事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgDayMatterUserBo bo) {
        List<String> userIdList = bo.getUserIdList();
        if (!userIdList.isEmpty()) {
            for (String userId : userIdList) {
                MsgDayMatterUser add = new MsgDayMatterUser();
                add.setUserId(Long.valueOf(userId));
                add.setDayMatterId(bo.getDayMatterId());
                if (validEntityBeforeSave(add)) {
                    baseMapper.insert(add);
                }
            }
        }
        return true;
    }

    /**
     * 修改事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgDayMatterUserBo bo) {
        List<String> userIdList = bo.getUserIdList();
        if (!userIdList.isEmpty()) {
            for (String userId : userIdList) {
                MsgDayMatterUser add = new MsgDayMatterUser();
                add.setUserId(Long.valueOf(userId));
                add.setDayMatterId(bo.getDayMatterId());
                add.setId(bo.getId());
                if (validEntityBeforeSave(add)) {
                    baseMapper.updateById(add);
                } else {
                    throw new ServiceException("用户事件不能重复");
                }
            }
        }
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private boolean validEntityBeforeSave(MsgDayMatterUser entity){
        return !baseMapper.exists(Wrappers.<MsgDayMatterUser>lambdaQuery()
            .eq(MsgDayMatterUser::getUserId, entity.getUserId())
            .eq(MsgDayMatterUser::getDayMatterId, entity.getDayMatterId())
            .ne(entity.getId() != null, MsgDayMatterUser::getId, entity.getId()));
    }

    /**
     * 校验并批量删除事件与用户关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
