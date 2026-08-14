package org.dromara.mes.msg.support;

import org.dromara.mes.enums.RemindTypeEnum;
import org.dromara.mes.utils.LunarSolarUtils;
import org.dromara.mes.utils.TimeCalculatorUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * 计算事件下次通知时间。无时分秒的日期型事件（生日、纪念日等）统一落到当天 06:00。
 */
public final class NotifyTimeHelper {

    /** 无具体时刻时的默认发送钟点 */
    public static final int DATE_ONLY_HOUR = 6;

    private NotifyTimeHelper() {
    }

    public static Date calculate(Date dayTarget, String remindType, String dayLunar) {
        if (dayTarget == null || remindType == null || remindType.isBlank()) {
            return null;
        }
        RemindTypeEnum typeEnum;
        try {
            typeEnum = RemindTypeEnum.fromCode(remindType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("提醒周期无效: " + remindType);
        }
        if (typeEnum == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean dateOnly = TimeCalculatorUtil.isZeroClock(dayTarget);
        LocalDateTime next;

        if (typeEnum == RemindTypeEnum.YEARLY && "lunar".equalsIgnoreCase(dayLunar)) {
            next = nextLunarYearly(dayTarget, now, dateOnly);
        } else {
            next = toLocalDateTime(dayTarget);
            if (dateOnly && isDatePeriod(typeEnum)) {
                next = next.toLocalDate().atTime(DATE_ONLY_HOUR, 0);
            }
            while (!next.isAfter(now)) {
                next = plusPeriod(next, typeEnum);
                if (dateOnly && isDatePeriod(typeEnum)) {
                    next = next.toLocalDate().atTime(DATE_ONLY_HOUR, 0);
                }
            }
        }
        return Date.from(next.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static boolean isDatePeriod(RemindTypeEnum typeEnum) {
        return typeEnum == RemindTypeEnum.DAILY
            || typeEnum == RemindTypeEnum.WEEKLY
            || typeEnum == RemindTypeEnum.MONTHLY
            || typeEnum == RemindTypeEnum.YEARLY;
    }

    private static LocalDateTime nextLunarYearly(Date dayTarget, LocalDateTime now, boolean dateOnly) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dayTarget);
        int lunarMonth = cal.get(Calendar.MONTH) + 1;
        int lunarDay = cal.get(Calendar.DAY_OF_MONTH);
        int tryYear = LocalDate.now().getYear();

        while (true) {
            Set<String> solarDates = LunarSolarUtils.lunarToSolarTryBoth(tryYear, lunarMonth, lunarDay);
            if (solarDates.isEmpty()) {
                throw new RuntimeException("农历转换失败: year=" + tryYear + ", month=" + lunarMonth + ", day=" + lunarDay);
            }
            Optional<LocalDateTime> future = solarDates.stream()
                .map(LocalDate::parse)
                .sorted()
                .map(d -> dateOnly ? d.atTime(DATE_ONLY_HOUR, 0) : d.atStartOfDay())
                .filter(dt -> dt.isAfter(now))
                .findFirst();
            if (future.isPresent()) {
                return future.get();
            }
            tryYear++;
        }
    }

    private static LocalDateTime plusPeriod(LocalDateTime next, RemindTypeEnum typeEnum) {
        return switch (typeEnum) {
            case MINUTELY -> next.plusMinutes(1);
            case HOURLY -> next.plusHours(1);
            case DAILY -> next.plusDays(1);
            case WEEKLY -> next.plusWeeks(1);
            case MONTHLY -> next.plusMonths(1);
            case YEARLY -> next.plusYears(1);
        };
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDateTime.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND)
        );
    }
}
