package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.enums.RemindTypeEnum;
import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.mes.msg.domain.vo.*;
import org.dromara.mes.msg.enums.WhetherFlag;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.msg.support.MsgMaintainerHelper;
import org.dromara.mes.utils.LunarSolarUtils;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.service.IMsgDayMatterService;
import org.dromara.mes.msg.support.MsgMailSender;
import org.springframework.util.CollectionUtils;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
    private final MsgMailSender msgMailSender;

    /** 日历事件短时缓存，按维护人分桶，避免翻月打库且防止用户间串数据 */
    private static final long CALENDAR_CACHE_TTL_MS = 30_000L;
    private final Map<Long, CalendarCacheEntry> calendarEventCacheByUser = new ConcurrentHashMap<>();

    private static final class CalendarCacheEntry {
        private final List<MsgDayMatterVo> events;
        private final long expireAt;

        private CalendarCacheEntry(List<MsgDayMatterVo> events, long expireAt) {
            this.events = events;
            this.expireAt = expireAt;
        }
    }

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
        MsgMaintainerHelper.apply(bo);
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
        MsgMaintainerHelper.apply(bo);
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
            invalidateCalendarEventCache();
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
            .eq(MsgDayMatter::getCreateBy, MsgMaintainerHelper.currentUserId())
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
        boolean updated = baseMapper.update(updateWrapper) > 0;
        if (updated) {
            invalidateCalendarEventCache();
        }
        return updated;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgDayMatterBo entity){
        boolean exists = baseMapper.exists(Wrappers.<MsgDayMatter>lambdaQuery()
            .eq(MsgDayMatter::getDayName, entity.getDayName())
            .eq(MsgDayMatter::getCreateBy, MsgMaintainerHelper.currentUserId())
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
        boolean deleted = baseMapper.delete(Wrappers.<MsgDayMatter>lambdaQuery()
            .in(MsgDayMatter::getId, ids)
            .eq(MsgDayMatter::getCreateBy, MsgMaintainerHelper.currentUserId())) > 0;
        if (deleted) {
            invalidateCalendarEventCache();
        }
        return deleted;
    }

    @Override
    public List<MsgDayMatterNameVo> queryDayNameList(String dayName, String id, PageQuery pageQuery) {
        return baseMapper.queryDayNameList(pageQuery.build(), dayName, id, MsgMaintainerHelper.currentUserId());
    }

    @Override
    public List<ReminderVo> dayMatterList(int year, int month, Long groupId) {
        // 日历展示按循环规则投影到查询月，不能用 next_notify_time 过滤（那是下次推送时间，循环事件常落在明年）
        List<MsgDayMatterVo> msgDayMatterVoList = loadAllEventsForCalendar();
        if (CollectionUtils.isEmpty(msgDayMatterVoList)) {
            return List.of();
        }

        List<Long> filteredDayMatterIds;
        if (groupId != null) {
            List<MsgMatterGroupVo> msgMatterGroupVoList = msgMatterGroupMapper.selectVoList(
                new LambdaQueryWrapper<MsgMatterGroup>()
                    .eq(MsgMatterGroup::getGroupId, groupId)
                    .eq(MsgMatterGroup::getCreateBy, MsgMaintainerHelper.currentUserId())
            );
            filteredDayMatterIds = msgMatterGroupVoList.stream()
                .map(MsgMatterGroupVo::getDayMatterId)
                .toList();
        } else {
            filteredDayMatterIds = null;
        }

        return msgDayMatterVoList.stream()
            .filter(item -> filteredDayMatterIds == null || filteredDayMatterIds.contains(item.getId()))
            .flatMap(item -> projectToMonth(item, year, month).stream())
            .collect(Collectors.toList());
    }

    /**
     * 读取当前登录人维护的日历事件，30 秒内按用户复用，增删改会主动失效。
     */
    private List<MsgDayMatterVo> loadAllEventsForCalendar() {
        Long userId = MsgMaintainerHelper.currentUserId();
        long now = System.currentTimeMillis();
        CalendarCacheEntry cached = calendarEventCacheByUser.get(userId);
        if (cached != null && now < cached.expireAt) {
            return cached.events;
        }
        synchronized (this) {
            CalendarCacheEntry again = calendarEventCacheByUser.get(userId);
            if (again != null && System.currentTimeMillis() < again.expireAt) {
                return again.events;
            }
            MsgDayMatterBo bo = new MsgDayMatterBo();
            MsgMaintainerHelper.apply(bo);
            Page<MsgDayMatterVo> page = baseMapper.queryPageList(new PageQuery().build(), bo);
            List<MsgDayMatterVo> list = page == null || CollectionUtils.isEmpty(page.getRecords())
                ? List.of()
                : page.getRecords();
            calendarEventCacheByUser.put(userId, new CalendarCacheEntry(list, System.currentTimeMillis() + CALENDAR_CACHE_TTL_MS));
            return list;
        }
    }

    private void invalidateCalendarEventCache() {
        calendarEventCacheByUser.clear();
    }

    /**
     * 将事件投影到指定年/月：循环事件按周期展开，非循环只展示原始日期。
     */
    private List<ReminderVo> projectToMonth(MsgDayMatterVo item, int year, int month) {
        if (item.getDayTarget() == null) {
            return List.of();
        }
        boolean lunar = "lunar".equalsIgnoreCase(item.getDayLunar());
        boolean repeat = WhetherFlag.YES.getCode().equals(item.getRepeatFlag());
        List<LocalDate> dates = repeat
            ? repeatingDates(item, year, month, lunar)
            : oneShotDates(item, year, month, lunar);
        return dates.stream()
            .distinct()
            .map(date -> toReminderVo(item, date, lunar))
            .collect(Collectors.toList());
    }

    /**
     * 非循环事件：只出现在原始目标日所在的那一个月。
     */
    private List<LocalDate> oneShotDates(MsgDayMatterVo item, int year, int month, boolean lunar) {
        if (lunar) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getDayTarget());
            return lunarSolarInMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH), year, month, null);
        }
        LocalDate target = toLocalDate(item.getDayTarget());
        if (target.getYear() == year && target.getMonthValue() == month) {
            return List.of(target);
        }
        return List.of();
    }

    /**
     * 循环事件：按提醒周期计算查询月内所有发生日。
     */
    private List<LocalDate> repeatingDates(MsgDayMatterVo item, int year, int month, boolean lunar) {
        RemindTypeEnum typeEnum;
        try {
            typeEnum = RemindTypeEnum.fromCode(item.getRemindType());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        if (typeEnum == null) {
            return List.of();
        }
        LocalDate start = lunar ? lunarStartSolar(item.getDayTarget()) : toLocalDate(item.getDayTarget());
        return switch (typeEnum) {
            case YEARLY -> yearlyDates(item, year, month, lunar, start);
            case MONTHLY -> ofDateIfValid(year, month, toLocalDate(item.getDayTarget()).getDayOfMonth(), start);
            case WEEKLY -> weeklyDates(year, month, start);
            case DAILY -> dailyDates(year, month, start);
            case HOURLY, MINUTELY -> {
                Date notify = item.getNextNotifyTime() != null ? item.getNextNotifyTime() : item.getDayTarget();
                LocalDate d = toLocalDate(notify);
                yield (d.getYear() == year && d.getMonthValue() == month) ? List.of(d) : List.of();
            }
        };
    }

    private List<LocalDate> yearlyDates(MsgDayMatterVo item, int year, int month, boolean lunar, LocalDate start) {
        if (lunar) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getDayTarget());
            return lunarSolarInMonth(year, cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH), year, month, start);
        }
        LocalDate target = toLocalDate(item.getDayTarget());
        return ofDateIfValid(year, target.getMonthValue(), target.getDayOfMonth(), start).stream()
            .filter(date -> date.getMonthValue() == month)
            .collect(Collectors.toList());
    }

    /**
     * 农历月日转到指定公历年，只保留落在查询月且不早于起始日的日期。
     */
    private List<LocalDate> lunarSolarInMonth(int lunarYear, int lunarMonth, int lunarDay,
                                              int queryYear, int queryMonth, LocalDate start) {
        Set<String> solarDates = LunarSolarUtils.lunarToSolarTryBoth(lunarYear, lunarMonth, lunarDay);
        List<LocalDate> result = new ArrayList<>();
        for (String solar : solarDates) {
            LocalDate date = LocalDate.parse(solar);
            if (date.getYear() == queryYear && date.getMonthValue() == queryMonth
                && (start == null || !date.isBefore(start))) {
                result.add(date);
            }
        }
        return result;
    }

    private LocalDate lunarStartSolar(Date dayTarget) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dayTarget);
        return LunarSolarUtils.lunarToSolarTryBoth(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
            .stream()
            .map(LocalDate::parse)
            .min(LocalDate::compareTo)
            .orElse(toLocalDate(dayTarget));
    }

    private List<LocalDate> ofDateIfValid(int year, int month, int day, LocalDate start) {
        try {
            LocalDate occurrence = LocalDate.of(year, month, day);
            if (!occurrence.isBefore(start)) {
                return List.of(occurrence);
            }
        } catch (DateTimeException ignored) {
            // 例如非闰年的 2 月 29 日，该年不展示
        }
        return List.of();
    }

    private List<LocalDate> weeklyDates(int year, int month, LocalDate start) {
        YearMonth ym = YearMonth.of(year, month);
        DayOfWeek dow = start.getDayOfWeek();
        List<LocalDate> dates = new ArrayList<>();
        LocalDate cursor = ym.atDay(1);
        while (!cursor.isAfter(ym.atEndOfMonth())) {
            if (cursor.getDayOfWeek() == dow && !cursor.isBefore(start)) {
                dates.add(cursor);
            }
            cursor = cursor.plusDays(1);
        }
        return dates;
    }

    private List<LocalDate> dailyDates(int year, int month, LocalDate start) {
        YearMonth ym = YearMonth.of(year, month);
        List<LocalDate> dates = new ArrayList<>();
        LocalDate cursor = ym.atDay(1);
        while (!cursor.isAfter(ym.atEndOfMonth())) {
            if (!cursor.isBefore(start)) {
                dates.add(cursor);
            }
            cursor = cursor.plusDays(1);
        }
        return dates;
    }

    private ReminderVo toReminderVo(MsgDayMatterVo item, LocalDate date, boolean lunar) {
        ReminderVo dto = new ReminderVo();
        dto.setContent(item.getDayName());
        dto.setType(item.getDayType());
        dto.setDate(date.toString());
        dto.setIsLunar(lunar);
        if (lunar) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getDayTarget());
            dto.setLunarMonth(cal.get(Calendar.MONTH) + 1);
            dto.setLunarDay(cal.get(Calendar.DAY_OF_MONTH));
        }
        return dto;
    }

    private LocalDate toLocalDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 扫描已到期事件：先发邮件，成功后再滚动下次时间；失败则保留到期时间以便重试。
     */
    @Override
    public void updateNextNotifyTime() {
        MsgDayMatterBo bo = new MsgDayMatterBo();
        bo.setNotifyEndTime(new Date());
        Page<MsgDayMatterVo> msgDayMatterVoPage = baseMapper.queryPageList(new PageQuery().build(), bo);
        if (msgDayMatterVoPage == null || CollectionUtils.isEmpty(msgDayMatterVoPage.getRecords())) {
            return;
        }
        msgDayMatterVoPage.getRecords().forEach(item -> {
            if (isNotifySkipped(item.getNotifyStatus())) {
                return;
            }
            // 先按当前到期时间发信，成功后再计算下一次，避免正文里出现“下下一次”
            if (!msgMailSender.sendDayMatter(item)) {
                return;
            }
            if (WhetherFlag.YES.getCode().equals(item.getRepeatFlag())) {
                item.calculateNextNotifyTime();
                baseMapper.update(new LambdaUpdateWrapper<MsgDayMatter>()
                    .eq(MsgDayMatter::getId, item.getId())
                    .set(MsgDayMatter::getNextNotifyTime, item.getNextNotifyTime())
                );
            } else {
                // 非循环发送成功后标为已通知，避免每分钟重发
                baseMapper.update(new LambdaUpdateWrapper<MsgDayMatter>()
                    .eq(MsgDayMatter::getId, item.getId())
                    .set(MsgDayMatter::getNotifyStatus, "notified")
                );
            }
        });
    }

    private boolean isNotifySkipped(String notifyStatus) {
        return "disabled".equals(notifyStatus)
            || "notified".equals(notifyStatus)
            || "expired".equals(notifyStatus);
    }

    /**
     * 测试发信：取最近创建的一条事件，只发邮件，不改下次通知时间。
     */
    @Override
    public String testSendLatestMail() {
        MsgDayMatterVo item = baseMapper.selectLatestOne();
        if (item == null) {
            throw new ServiceException("没有可发送的事件");
        }
        boolean sent = msgMailSender.sendDayMatter(item);
        if (!sent) {
            throw new ServiceException("发送失败：" + item.getDayName());
        }
        return "已发送：" + item.getDayName();
    }
}
