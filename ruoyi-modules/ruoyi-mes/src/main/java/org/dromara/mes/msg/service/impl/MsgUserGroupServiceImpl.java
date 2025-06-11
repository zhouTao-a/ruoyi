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
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.mes.msg.domain.vo.MsgUserGroupVo;
import org.dromara.mes.msg.domain.MsgUserGroup;
import org.dromara.mes.msg.mapper.MsgUserGroupMapper;
import org.dromara.mes.msg.service.IMsgUserGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户组Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgUserGroupServiceImpl implements IMsgUserGroupService {

    private final MsgUserGroupMapper baseMapper;

    /**
     * 查询用户组
     *
     * @param id 主键
     * @return 用户组
     */
    @Override
    public MsgUserGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户组分页列表
     */
    @Override
    public TableDataInfo<MsgUserGroupVo> queryPageList(MsgUserGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MsgUserGroup> lqw = buildQueryWrapper(bo);
        Page<MsgUserGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户组列表
     *
     * @param bo 查询条件
     * @return 用户组列表
     */
    @Override
    public List<MsgUserGroupVo> queryList(MsgUserGroupBo bo) {
        LambdaQueryWrapper<MsgUserGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgUserGroup> buildQueryWrapper(MsgUserGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MsgUserGroup> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MsgUserGroup::getId);
        lqw.eq(bo.getUserId() != null, MsgUserGroup::getUserId, bo.getUserId());
        lqw.eq(bo.getGroupId() != null, MsgUserGroup::getGroupId, bo.getGroupId());
        lqw.eq(bo.getRelativeGenerationDiff() != null, MsgUserGroup::getRelativeGenerationDiff, bo.getRelativeGenerationDiff());
        lqw.eq(StringUtils.isNotBlank(bo.getKinshipLevel()), MsgUserGroup::getKinshipLevel, bo.getKinshipLevel());
        return lqw;
    }

    /**
     * 新增用户组
     *
     * @param bo 用户组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgUserGroupBo bo) {
        MsgUserGroup add = MapstructUtils.convert(bo, MsgUserGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户组
     *
     * @param bo 用户组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgUserGroupBo bo) {
        MsgUserGroup update = MapstructUtils.convert(bo, MsgUserGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgUserGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除用户组信息
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
