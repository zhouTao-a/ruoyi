package org.dromara.mes.msg.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 是否标志枚举
 * T 表示 是（true），F 表示 否（false）
 */
@Getter
public enum WhetherFlag {

    YES("T", "是"),
    NO("F", "否");

    @JsonValue
    private final String code;
    private final String label;

    WhetherFlag(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static WhetherFlag fromCode(String code) {
        for (WhetherFlag flag : values()) {
            if (flag.code.equalsIgnoreCase(code)) {
                return flag;
            }
        }
        throw new IllegalArgumentException("未知是否标志: " + code);
    }

    @Override
    public String toString() {
        return this.label;
    }
}
