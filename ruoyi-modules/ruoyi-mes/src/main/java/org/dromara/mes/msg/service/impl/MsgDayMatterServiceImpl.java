package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.msg.domain.vo.MsgDayMatterNameVo;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.service.IMsgDayMatterService;

import java.util.Date;
import java.util.List;
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
        Page<MsgDayMatterVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
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
        return baseMapper.queryPageList(new PageQuery().build(), bo).getRecords();
    }

    /**
     * 新增事件
     *
     * @param bo 事件
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgDayMatterBo bo) {
        validEntityBeforeSave(bo);
        MsgDayMatter add = MapstructUtils.convert(bo, MsgDayMatter.class);
        assert add != null;
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
        validEntityBeforeSave(bo);
        LambdaUpdateWrapper<MsgDayMatter> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MsgDayMatter::getId, bo.getId())
            .set(MsgDayMatter::getDayName, bo.getDayName())
            .set(MsgDayMatter::getDayTarget, bo.getDayTarget())
            .set(MsgDayMatter::getDayLunar, bo.getDayLunar())
            .set(MsgDayMatter::getDayType, bo.getDayType())
            .set(MsgDayMatter::getRemindType, bo.getRemindType())
            .set(MsgDayMatter::getRepeatFlag, bo.getRepeatFlag())
            .set(MsgDayMatter::getNotifyStatus, bo.getNotifyStatus())
            .set(MsgDayMatter::getNextNotifyTime, bo.getNextNotifyTime())
            .set(MsgDayMatter::getUserId, bo.getUserId())
            .set(MsgDayMatter::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgDayMatterBo entity){
        List<MsgDayMatterVo> msgDayMatterVoList = baseMapper.selectVoList(Wrappers.<MsgDayMatter>lambdaQuery()
            .eq(MsgDayMatter::getDayName, entity.getDayName())
            .ne(entity.getId() != null, MsgDayMatter::getId, entity.getId()));
        if (!msgDayMatterVoList.isEmpty()) {
            throw new ServiceException("事件名称不能重复!");
        }
        entity.calculateNextNotifyTime();
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
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<MsgDayMatterNameVo> queryDayNameList(String dayName, String id, PageQuery pageQuery) {
        return baseMapper.queryDayNameList(pageQuery.build(), dayName, id);
    }
}
