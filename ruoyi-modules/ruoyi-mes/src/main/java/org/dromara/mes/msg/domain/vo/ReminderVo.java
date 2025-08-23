package org.dromara.mes.msg.domain.vo;

import lombok.Data;

@Data
public class ReminderVo {
    /**
     * 公历日期字符串
     */
    private String date;

    /**
     * 是否农历
     */
    private Boolean isLunar;

    /**
     * 农历月（如果是农历）
     */
    private Integer lunarMonth;

    /**
     * 农历日（如果是农历）
     */
    private Integer lunarDay;

    /**
     * 类型
     */
    private String type;

    /**
     * 事件名称
     */
    private String content;
}
