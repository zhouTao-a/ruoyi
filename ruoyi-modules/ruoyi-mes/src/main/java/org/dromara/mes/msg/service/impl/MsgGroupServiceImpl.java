package org.dromara.mes.msg.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.mes.msg.domain.vo.MsgGroupVo;
import org.dromara.mes.msg.domain.MsgGroup;
import org.dromara.mes.msg.mapper.MsgGroupMapper;
import org.dromara.mes.msg.service.IMsgGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 分组信息Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgGroupServiceImpl implements IMsgGroupService {

    private final MsgGroupMapper baseMapper;

    /**
     * 查询分组信息
     *
     * @param id 主键
     * @return 分组信息
     */
    @Override
    public MsgGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询分组信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分组信息分页列表
     */
    @Override
    public TableDataInfo<MsgGroupVo> queryPageList(MsgGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MsgGroup> lqw = buildQueryWrapper(bo);
        Page<MsgGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的分组信息列表
     *
     * @param bo 查询条件
     * @return 分组信息列表
     */
    @Override
    public List<MsgGroupVo> queryList(MsgGroupBo bo) {
        LambdaQueryWrapper<MsgGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgGroup> buildQueryWrapper(MsgGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MsgGroup> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MsgGroup::getId);
        lqw.like(StringUtils.isNotBlank(bo.getGroupName()), MsgGroup::getGroupName, bo.getGroupName());
        lqw.eq(StringUtils.isNotBlank(bo.getGroupCode()), MsgGroup::getGroupCode, bo.getGroupCode());
        return lqw;
    }

    /**
     * 新增分组信息
     *
     * @param bo 分组信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgGroupBo bo) {
        MsgGroup add = MapstructUtils.convert(bo, MsgGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改分组信息
     *
     * @param bo 分组信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgGroupBo bo) {
        MsgGroup update = MapstructUtils.convert(bo, MsgGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除分组信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
