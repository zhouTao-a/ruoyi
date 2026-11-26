package org.dromara.mes.msg.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 手机本地闹钟用的事件。只含待通知数据。
 * 邮件和手机都按 nextNotifyTime 的时刻，不再把 00:00:00 改点。
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
     * 事件原始时间。
     */
    private Date dayTarget;

    /**
     * 下一次提醒时间。邮件和手机都用这个时刻。
     */
    private Date nextNotifyTime;
}
