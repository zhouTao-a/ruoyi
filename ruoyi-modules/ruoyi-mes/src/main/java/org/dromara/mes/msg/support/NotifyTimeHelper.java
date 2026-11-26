package org.dromara.mes.msg.support;

import org.dromara.mes.enums.RemindTypeEnum;
import org.dromara.mes.utils.LunarSolarUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * 计算事件下次通知时间。时分秒沿用事件时间，00:00:00 也保持 00:00:00。
 */
public final class NotifyTimeHelper {

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
        LocalDateTime next;

        if (typeEnum == RemindTypeEnum.YEARLY && "lunar".equalsIgnoreCase(dayLunar)) {
            next = nextLunarYearly(dayTarget, now);
        } else {
            next = toLocalDateTime(dayTarget);
            while (!next.isAfter(now)) {
                next = plusPeriod(next, typeEnum);
            }
        }
        return Date.from(next.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime nextLunarYearly(Date dayTarget, LocalDateTime now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dayTarget);
        int lunarMonth = cal.get(Calendar.MONTH) + 1;
        int lunarDay = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int tryYear = LocalDate.now().getYear();

        while (true) {
            Set<String> solarDates = LunarSolarUtils.lunarToSolarTryBoth(tryYear, lunarMonth, lunarDay);
            if (solarDates.isEmpty()) {
                throw new RuntimeException("农历转换失败: year=" + tryYear + ", month=" + lunarMonth + ", day=" + lunarDay);
            }
            Optional<LocalDateTime> future = solarDates.stream()
                .map(LocalDate::parse)
                .sorted()
                .map(d -> d.atTime(hour, minute, second))
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
