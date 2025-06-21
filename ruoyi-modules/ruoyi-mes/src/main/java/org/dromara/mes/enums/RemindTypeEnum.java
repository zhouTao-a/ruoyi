package org.dromara.mes.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 提醒周期枚举
 */
public enum RemindTypeEnum {

    /**
     * 每分钟提醒
     */
    MINUTELY("minutely", "每分"),

    /**
     * 每小时提醒
     */
    HOURLY("hourly", "每时"),

    /**
     * 每天提醒
     */
    DAILY("daily", "每天"),

    /**
     * 每周提醒
     */
    WEEKLY("weekly", "每周"),

    /**
     * 每月提醒
     */
    MONTHLY("monthly", "每月"),

    /**
     * 每年提醒
     */
    YEARLY("yearly", "每年");

    private final String code;
    private final String description;

    RemindTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** 获取英文代码（如 minutely） */
    @JsonValue
    public String getCode() {
        return code;
    }

    /** 获取中文描述（如 每分钟） */
    @JsonValue
    public String getDescription() {
        return description;
    }

    /**
     * 根据 code（如 "daily"）获取枚举对象
     */
    public static RemindTypeEnum fromCode(String code) {
        if (code == null) return null;
        for (RemindTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的提醒周期 code: " + code);
    }
}
