package org.dromara.mes.msg.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.mes.msg.domain.vo.MsgMatterGroupVo;
import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.msg.service.IMsgMatterGroupService;

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
        LambdaQueryWrapper<MsgMatterGroup> lqw = buildQueryWrapper(bo);
        Page<MsgMatterGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
        LambdaQueryWrapper<MsgMatterGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgMatterGroup> buildQueryWrapper(MsgMatterGroupBo bo) {
        LambdaQueryWrapper<MsgMatterGroup> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MsgMatterGroup::getId);
        lqw.eq(bo.getMatterId() != null, MsgMatterGroup::getMatterId, bo.getMatterId());
        lqw.eq(bo.getGroupId() != null, MsgMatterGroup::getGroupId, bo.getGroupId());
        return lqw;
    }

    /**
     * 新增事件组
     *
     * @param bo 事件组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgMatterGroupBo bo) {
        MsgMatterGroup add = MapstructUtils.convert(bo, MsgMatterGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            assert add != null;
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改事件组
     *
     * @param bo 事件组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgMatterGroupBo bo) {
        MsgMatterGroup update = MapstructUtils.convert(bo, MsgMatterGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgMatterGroup entity){
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
        return baseMapper.deleteByIds(ids) > 0;
    }
}
