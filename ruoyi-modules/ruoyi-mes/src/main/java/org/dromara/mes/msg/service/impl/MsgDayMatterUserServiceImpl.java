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
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterUserVo;
import org.dromara.mes.msg.domain.MsgDayMatterUser;
import org.dromara.mes.msg.mapper.MsgDayMatterUserMapper;
import org.dromara.mes.msg.service.IMsgDayMatterUserService;

import java.util.List;
import java.util.Map;
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
        LambdaQueryWrapper<MsgDayMatterUser> lqw = buildQueryWrapper(bo);
        Page<MsgDayMatterUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
        LambdaQueryWrapper<MsgDayMatterUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgDayMatterUser> buildQueryWrapper(MsgDayMatterUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MsgDayMatterUser> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MsgDayMatterUser::getId);
        lqw.eq(bo.getDayMatterId() != null, MsgDayMatterUser::getDayMatterId, bo.getDayMatterId());
        lqw.eq(bo.getUserId() != null, MsgDayMatterUser::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgDayMatterUserBo bo) {
        MsgDayMatterUser add = MapstructUtils.convert(bo, MsgDayMatterUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgDayMatterUserBo bo) {
        MsgDayMatterUser update = MapstructUtils.convert(bo, MsgDayMatterUser.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgDayMatterUser entity){
        //TODO 做一些数据校验,如唯一约束
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
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
