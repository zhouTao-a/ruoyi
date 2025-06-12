package org.dromara.mes.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日期格式化工具类
 * 提供线程安全的日期格式转换、Date 与 LocalDate/LocalDateTime 相互转换功能。
 * 适用于替代 SimpleDateFormat，避免线程安全问题。
 */
public class DateFormatUtils {

    /**
     * 私有构造方法，防止工具类被实例化
     */
    private DateFormatUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 线程安全的日期格式化器：yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 线程安全的日期时间格式化器：yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 系统默认时区，用于 Date 与 LocalDate/LocalDateTime 之间的转换
     */
    public static final ZoneId ZONE = ZoneId.systemDefault();

    /**
     * 将 Date 格式化为字符串，格式为 yyyy-MM-dd
     *
     * @param date 要格式化的 Date 对象
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        return DATE_FORMATTER.format(date.toInstant().atZone(ZONE).toLocalDate());
    }

    /**
     * 将 Date 格式化为字符串，格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param date 要格式化的 Date 对象
     * @return 格式化后的字符串
     */
    public static String formatDateTime(Date date) {
        return DATE_TIME_FORMATTER.format(date.toInstant().atZone(ZONE));
    }

    /**
     * 将 LocalDate 和时间拼装为 Date
     *
     * @param localDate LocalDate 日期部分
     * @param hour      小时（0-23）
     * @param minute    分钟（0-59）
     * @param second    秒（0-59）
     * @return 转换后的 Date 对象
     */
    public static Date toDate(LocalDate localDate, int hour, int minute, int second) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(hour, minute, second));
        return Date.from(dateTime.atZone(ZONE).toInstant());
    }

    /**
     * 将 LocalDateTime 转换为 Date
     *
     * @param localDateTime 本地时间
     * @return Date 类型
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZONE).toInstant());
    }

    /**
     * 将 Date 转换为 LocalDate（仅保留年月日）
     *
     * @param date Date 类型
     * @return LocalDate 类型
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZONE).toLocalDate();
    }

    /**
     * 将 Date 转换为 LocalDateTime（包含年月日时分秒）
     *
     * @param date Date 类型
     * @return LocalDateTime 类型
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZONE).toLocalDateTime();
    }
}
