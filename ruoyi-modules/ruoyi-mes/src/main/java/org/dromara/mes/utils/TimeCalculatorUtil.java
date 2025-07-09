package org.dromara.mes.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间范围计算工具类 (左闭右开区间)
 * 特征：
 * 1. 严格使用 start <= ts < end 的时间范围模式
 * 2. 支持日、周、月、年四种时间维度
 * 3. 内置时区处理（默认上海时区）
 * 4. 线程安全
 */
public class TimeCalculatorUtil {

    public enum RangeType {HOUR, DAY, WEEK, MONTH, YEAR }

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");

    // ------------------ 对外接口（LocalDate版） ------------------

    /**
     * 基于LocalDate计算时间范围（默认时区）
     * @param baseDate 基准日期
     * @param rangeType 时间维度类型
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(LocalDate baseDate, RangeType rangeType) {
        return calculateRange(baseDate, rangeType, DEFAULT_ZONE);
    }

    /**
     * 基于LocalDate计算时间范围（指定时区）
     * @param baseDate 基准日期
     * @param rangeType 时间维度类型
     * @param zone 目标时区
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(LocalDate baseDate, RangeType rangeType, ZoneId zone) {
        validateParams(baseDate, rangeType, zone);
        return calculateRangeInternal(baseDate, rangeType, zone);
    }

    // ------------------ 对外接口（字符串版） ------------------

    /**
     * 基于日期字符串计算时间范围（默认时区）
     * @param dateStr 日期字符串（yyyy-M-d）
     * @param rangeType 时间维度类型
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(String dateStr, RangeType rangeType) {
        return calculateRange(dateStr, rangeType, DEFAULT_ZONE);
    }

    /**
     * 基于日期字符串计算时间范围（指定时区）
     * @param dateStr 日期字符串（yyyy-M-d）
     * @param rangeType 时间维度类型
     * @param zone 目标时区
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(String dateStr, RangeType rangeType, ZoneId zone) {
        validateParams(dateStr, rangeType, zone);
        LocalDate baseDate = parseDate(dateStr);
        return calculateRangeInternal(baseDate, rangeType, zone);
    }

    // ------------------ 对外接口（LocalDateTime版，支持HOUR） ------------------

    /**
     * 基于LocalDateTime计算时间范围（仅支持HOUR，默认时区）
     * @param baseDateTime 基准日期时间
     * @param rangeType 时间维度类型
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(LocalDateTime baseDateTime, RangeType rangeType) {
        return calculateRange(baseDateTime, rangeType, DEFAULT_ZONE);
    }

    /**
     * 基于LocalDateTime计算时间范围（仅支持HOUR，指定时区）
     * @param baseDateTime 基准日期时间
     * @param rangeType 时间维度类型
     * @param zone 目标时区
     * @return [startInclusive, endExclusive)
     */
    public static long[] calculateRange(LocalDateTime baseDateTime, RangeType rangeType, ZoneId zone) {
        if (rangeType != RangeType.HOUR) {
            throw new UnsupportedOperationException("LocalDateTime只支持HOUR类型");
        }
        if (baseDateTime == null) {
            throw new IllegalArgumentException("baseDateTime不能为空");
        }
        if (zone == null) {
            zone = DEFAULT_ZONE;
        }
        return handleHour(baseDateTime, zone);
    }

    // ------------------ 核心计算逻辑 ------------------

    private static long[] calculateRangeInternal(LocalDate baseDate, RangeType type, ZoneId zone) {
        ZonedDateTime zonedBase = baseDate.atStartOfDay(zone);

        return switch (type) {
            case HOUR -> handleHour(zonedBase.toLocalDateTime(), zone);
            case DAY -> handleDay(zonedBase);
            case WEEK -> handleWeek(baseDate, zone);
            case MONTH -> handleMonth(zonedBase);
            case YEAR -> handleYear(zonedBase);
        };
    }


    // ------------------ 各维度处理器 ------------------

    private static long[] handleDay(ZonedDateTime base) {
        return new long[]{
            base.toInstant().toEpochMilli(),
            base.plusDays(1).toInstant().toEpochMilli()
        };
    }

    private static long[] handleWeek(LocalDate baseDate, ZoneId zone) {
        LocalDate weekStart = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusWeeks(1);
        return new long[]{
            weekStart.atStartOfDay(zone).toInstant().toEpochMilli(),
            weekEnd.atStartOfDay(zone).toInstant().toEpochMilli()
        };
    }

    private static long[] handleMonth(ZonedDateTime base) {
        ZonedDateTime monthStart = base.withDayOfMonth(1);
        return new long[]{
            monthStart.toInstant().toEpochMilli(),
            monthStart.plusMonths(1).toInstant().toEpochMilli()
        };
    }

    private static long[] handleYear(ZonedDateTime base) {
        ZonedDateTime yearStart = base.withDayOfYear(1);
        return new long[]{
            yearStart.toInstant().toEpochMilli(),
            yearStart.plusYears(1).toInstant().toEpochMilli()
        };
    }

    // ------------------ 各维度处理器（补充HOUR） ------------------

    /**
     * 计算某小时的左闭右开毫秒区间
     */
    private static long[] handleHour(LocalDateTime baseDateTime, ZoneId zone) {
        long start = baseDateTime.atZone(zone).toInstant().toEpochMilli();
        long end = baseDateTime.plusHours(1).atZone(zone).toInstant().toEpochMilli();
        return new long[]{start, end};
    }

    // ------------------ 工具方法 ------------------

    private static LocalDate parseDate(String dateStr) {
        validateDateString(dateStr);
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr
                + ", required pattern: yyyy-M-d");
        }
    }

    private static void validateParams(Object dateParam, RangeType type, ZoneId zone) {
        if (type == null) {
            throw new IllegalArgumentException("RangeType cannot be null");
        }
        validateParams(dateParam, zone);
    }

    private static void validateParams(Object dateParam, ZoneId zone) {
        if (dateParam == null) {
            throw new IllegalArgumentException("Date parameter cannot be null");
        }
        if (zone == null) {
            throw new IllegalArgumentException("ZoneId cannot be null");
        }
    }

    // ================== 单日时间戳获取 ==================

    /**
     * 智能解析日期字符串，返回对应的时间戳（毫秒）
     * 支持yyyy-M-d 或 yyyy-M-d HH:mm:ss
     */
    public static long parseToTs(String dateStr) {
        return parseToTs(dateStr, DEFAULT_ZONE);
    }

    public static long parseToTs(String dateStr, ZoneId zone) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("日期字符串不能为空");
        }
        // 判断格式
        if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            return getDailyStartTs(dateStr);
        } else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            // 有时分秒
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, DATETIME_FORMATTER);
            return dateTime.atZone(zone).toInstant().toEpochMilli();
        } else {
            throw new IllegalArgumentException("非法日期格式，支持：yyyy-M-d 或 yyyy-M-d HH:mm:ss，实际：" + dateStr);
        }
    }

    /**
     * 获取指定日期的零点时间戳（默认时区）
     * @param dateStr 日期字符串（格式：yyyy-M-d）
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getDailyStartTs(String dateStr) {
        return getDailyStartTs(dateStr, DEFAULT_ZONE);
    }

    /**
     * 获取指定日期的零点时间戳（指定时区）
     * @param dateStr 日期字符串（格式：yyyy-M-d）
     * @param zone 目标时区
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getDailyStartTs(String dateStr, ZoneId zone) {
        validateParams(dateStr, zone);
        LocalDate date = parseDate(dateStr);
        return date.atStartOfDay(zone).toInstant().toEpochMilli();
    }

    /**
     * 获取指定LocalDate的零点时间戳（默认时区）
     * @param date LocalDate对象
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getDailyStartTs(LocalDate date) {
        return getDailyStartTs(date, DEFAULT_ZONE);
    }

    /**
     * 获取指定LocalDate的零点时间戳（指定时区）
     * @param date LocalDate对象
     * @param zone 目标时区
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getDailyStartTs(LocalDate date, ZoneId zone) {
        validateParams(date, zone);
        return date.atStartOfDay(zone).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期+1天的零点时间戳（默认时区）
     * @param dateStr 日期字符串（格式：yyyy-M-d）
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getNextDayStartTs(String dateStr) {
        return getNextDayStartTs(dateStr, DEFAULT_ZONE);
    }

    /**
     * 获取指定日期+1天的零点时间戳（指定时区）
     * @param dateStr 日期字符串（格式：yyyy-M-d）
     * @param zone 目标时区
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getNextDayStartTs(String dateStr, ZoneId zone) {
        validateDateString(dateStr);
        LocalDate endDate = parseDate(dateStr);
        return getNextDayStartTs(endDate, zone);
    }

    /**
     * 获取指定LocalDate+1天的零点时间戳（默认时区）
     * @param date LocalDate对象
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getNextDayStartTs(LocalDate date) {
        return getNextDayStartTs(date, DEFAULT_ZONE);
    }

    /**
     * 获取指定LocalDate+1天的零点时间戳（指定时区）
     * @param date LocalDate对象
     * @param zone 目标时区
     * @return 当日00:00:00的时间戳（毫秒）
     */
    public static long getNextDayStartTs(LocalDate date, ZoneId zone) {
        validateParams(date, zone);
        LocalDate nextDay = date.plusDays(1);
        return nextDay.atStartOfDay(zone).toInstant().toEpochMilli();
    }

    private static void validateDateString(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("日期字符串不能为空");
        }
        if (!dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            throw new IllegalArgumentException("非法日期格式");
        }
    }

    /**
     * 获取指定日期维度的范围字符串（格式：yyyy-MM-dd）
     * @param dateStr 传入日期字符串（格式：yyyy-M-d 或 yyyy-MM-dd）
     * @param type 时间维度类型（日、周、月、年）
     * @return [开始日期, 结束日期]
     */
    public static String[] getRangeDateStr(String dateStr, RangeType type) {
        long[] range = calculateRange(dateStr, type);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(DEFAULT_ZONE);
        return new String[]{
            formatter.format(Instant.ofEpochMilli(range[0])),
            formatter.format(Instant.ofEpochMilli(range[1]))
        };
    }

    /**
     * 获取指定 Date 类型的范围字符串（格式：yyyy-MM-dd）
     * @param date Date 对象
     * @param type 时间维度类型（日、周、月、年）
     * @return [开始日期, 结束日期]
     */
    public static String[] getRangeDateStr(Date date, RangeType type) {
        if (date == null) {
            throw new IllegalArgumentException("date 不能为空");
        }
        // 将 Date 转为 LocalDate（使用默认时区）
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(DEFAULT_ZONE).toLocalDate();
        return getRangeDateStr(localDate, type);
    }

    public static String[] getRangeDateStr(LocalDate date, RangeType type) {
        long[] range = calculateRange(date, type);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(DEFAULT_ZONE);
        return new String[]{
            formatter.format(Instant.ofEpochMilli(range[0])),
            formatter.format(Instant.ofEpochMilli(range[1]))
        };
    }

    /**
     * 获取指定 LocalDate 范围的起止时间（Date[]）
     */
    public static Date[] getRangeDate(LocalDate baseDate, RangeType type) {
        long[] range = calculateRange(baseDate, type, DEFAULT_ZONE);
        return new Date[]{ new Date(range[0]), new Date(range[1]) };
    }

    /**
     * 获取指定字符串日期的范围起止时间（Date[]）
     * @param dateStr 格式：yyyy-M-d
     */
    public static Date[] getRangeDate(String dateStr, RangeType type) {
        long[] range = calculateRange(dateStr, type, DEFAULT_ZONE);
        return new Date[]{ new Date(range[0]), new Date(range[1]) };
    }

    /**
     * 获取指定 Date 类型的范围起止时间（Date[]）
     */
    public static Date[] getRangeDate(Date date, RangeType type) {
        if (date == null) {
            throw new IllegalArgumentException("date 不能为空");
        }
        LocalDate localDate = date.toInstant().atZone(DEFAULT_ZONE).toLocalDate();
        return getRangeDate(localDate, type);
    }





    /**
     * 测试
     * @param args 参数
     */
    public static void main(String[] args) {
        // 定义日期格式
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 测试示例
        testCase(LocalDate.now(), RangeType.DAY);    // 日范围
        testCase("2025-5-20", RangeType.WEEK);   // 周范围
        testCase(LocalDate.now().plusMonths(-1), RangeType.MONTH);  // 月范围
        testCase("2025-05-2", RangeType.YEAR);   // 年范围

        System.out.println(parseToTs("2025-5-20")); // 零点
        System.out.println(parseToTs("2025-5-20 13:14:15")); // 指定时分秒
        System.out.println(ymdhms.format(parseToTs("2025-5-20"))); // 零点
        System.out.println(ymdhms.format(parseToTs("2025-5-20 13:14:15"))); // 指定时分秒
    }

    private static void testCase(Object dateStr, RangeType type) {
        long[] range;
        if (dateStr instanceof LocalDate) {
            range = calculateRange((LocalDate) dateStr, type);
        }else {
            range = calculateRange((String) dateStr, type);
        }
        // 定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        System.out.printf("%s范围 [%s - %s)[%d - %d)%n",
            type.name(),
            sdf.format(new Date(range[0])),
            sdf.format(new Date(range[1])),
            range[0],
            range[1]
        );
    }

    /**
     * 判断给定时间是否是零点（00:00:00）
     */
    public static boolean isZeroClock(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        // 判断是否为 00:00:00.000
        return hour == 0 && minute == 0 && second == 0 && millisecond == 0;
    }
}
