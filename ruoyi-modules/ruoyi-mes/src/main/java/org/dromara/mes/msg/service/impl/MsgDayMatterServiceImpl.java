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
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.service.IMsgDayMatterService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 事件Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgDayMatterServiceImpl implements IMsgDayMatterService {

    private final MsgDayMatterMapper baseMapper;

    /**
     * 查询事件
     *
     * @param id 主键
     * @return 事件
     */
    @Override
    public MsgDayMatterVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询事件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件分页列表
     */
    @Override
    public TableDataInfo<MsgDayMatterVo> queryPageList(MsgDayMatterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MsgDayMatter> lqw = buildQueryWrapper(bo);
        Page<MsgDayMatterVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的事件列表
     *
     * @param bo 查询条件
     * @return 事件列表
     */
    @Override
    public List<MsgDayMatterVo> queryList(MsgDayMatterBo bo) {
        LambdaQueryWrapper<MsgDayMatter> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgDayMatter> buildQueryWrapper(MsgDayMatterBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MsgDayMatter> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MsgDayMatter::getId);
        lqw.like(StringUtils.isNotBlank(bo.getDayName()), MsgDayMatter::getDayName, bo.getDayName());
        lqw.eq(StringUtils.isNotBlank(bo.getDayType()), MsgDayMatter::getDayType, bo.getDayType());
        lqw.ge(bo.getNextNotifyTime() != null, MsgDayMatter::getNextNotifyTime, bo.getNextNotifyTime());
        lqw.eq(bo.getUserId() != null, MsgDayMatter::getUserId, bo.getUserId());
        lqw.eq(bo.getGroupId() != null, MsgDayMatter::getGroupId, bo.getGroupId());
        return lqw;
    }

    /**
     * 新增事件
     *
     * @param bo 事件
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgDayMatterBo bo) {
        MsgDayMatter add = MapstructUtils.convert(bo, MsgDayMatter.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改事件
     *
     * @param bo 事件
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgDayMatterBo bo) {
        MsgDayMatter update = MapstructUtils.convert(bo, MsgDayMatter.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgDayMatter entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除事件信息
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
