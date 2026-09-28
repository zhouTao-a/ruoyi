package org.dromara.mes.msg.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 手机本地闹钟用的事件。只含待通知数据。
 * 邮件仍按 nextNotifyTime 发送（无钟点时为 06:00）；手机另行用 dayTarget 判断是否改到上午 10 点。
 */
@Data
public class PhoneAlarmVo {

    /**
     * 事件 ID。用字符串避免前端大整数精度丢失。
     */
    private String id;

    /**
     * 事件名称，到点显示在通知和响铃页。
     */
    private String dayName;

    /**
     * 事件原始时间。时分秒为 00:00:00 表示没选具体钟点。
     */
    private Date dayTarget;

    /**
     * 下一次提醒日（邮件用的时刻）。手机只取这里的日期，钟点另算。
     */
    private Date nextNotifyTime;
}
