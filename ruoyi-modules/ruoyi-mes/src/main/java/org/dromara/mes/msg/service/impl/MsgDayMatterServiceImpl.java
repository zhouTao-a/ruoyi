package org.dromara.mes.msg.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.mes.msg.domain.vo.*;
import org.dromara.mes.msg.enums.WhetherFlag;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.utils.TimeCalculatorUtil;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.service.IMsgDayMatterService;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

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
    private final MsgMatterGroupMapper msgMatterGroupMapper;

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
        boolean exists = baseMapper.exists(Wrappers.<MsgDayMatter>lambdaQuery()
            .eq(MsgDayMatter::getDayName, entity.getDayName())
            .ne(entity.getId() != null, MsgDayMatter::getId, entity.getId()));
        if (exists) {
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

    @Override
    public List<ReminderVo> dayMatterList(int year, int month, Long groupId) {
        Date[] rangeDateStr = TimeCalculatorUtil.getRangeDate((year + "-" + month + "-01"), TimeCalculatorUtil.RangeType.MONTH);
        MsgDayMatterBo bo = new MsgDayMatterBo();
        bo.setNotifyStartTime(rangeDateStr[0]);
        bo.setNotifyEndTime(rangeDateStr[1]);
        Page<MsgDayMatterVo> msgDayMatterVoPage = baseMapper.queryPageList(new PageQuery().build(), bo);
        List<MsgDayMatterVo> msgDayMatterVoList = msgDayMatterVoPage.getRecords();
        if (CollectionUtils.isEmpty(msgDayMatterVoList)) {
            return List.of();
        }

        List<Long> filteredDayMatterIds;
        if (groupId != null) {
            // 1. 查询属于该 groupId 的所有 dayMatterId
            List<MsgMatterGroupVo> msgMatterGroupVoList = msgMatterGroupMapper.selectVoList(
                new LambdaQueryWrapper<MsgMatterGroup>().eq(MsgMatterGroup::getGroupId, groupId)
            );

            filteredDayMatterIds = msgMatterGroupVoList.stream()
                .map(MsgMatterGroupVo::getDayMatterId)
                .toList();
        } else {
            filteredDayMatterIds = null;
        }

        return msgDayMatterVoList.stream()
            // 2. 只保留属于该 group 的事件（如果 groupId 存在）
            .filter(item -> filteredDayMatterIds == null || filteredDayMatterIds.contains(item.getId()))
            // 3. 转换成 ReminderVo
            .map(item -> {
                ReminderVo dto = new ReminderVo();
                dto.setContent(item.getDayName());
                dto.setType(item.getDayType());
                dto.setIsLunar(false);
                dto.setDate(DateUtil.format(item.getNextNotifyTime(), "yyyy-MM-dd"));
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public void updateNextNotifyTime() {
        MsgDayMatterBo bo = new MsgDayMatterBo();
        bo.setNotifyEndTime(new Date());
        Page<MsgDayMatterVo> msgDayMatterVoPage = baseMapper.queryPageList(new PageQuery().build(), bo);
        msgDayMatterVoPage.getRecords().forEach(item -> {
            item.calculateNextNotifyTime();
            if (WhetherFlag.YES.getCode().equals(item.getRepeatFlag())) {
                baseMapper.update(new LambdaUpdateWrapper<MsgDayMatter>()
                    .eq(MsgDayMatter::getId, item.getId())
                    .set(MsgDayMatter::getNextNotifyTime, item.getNextNotifyTime())
                );
            }
        });
    }
}
