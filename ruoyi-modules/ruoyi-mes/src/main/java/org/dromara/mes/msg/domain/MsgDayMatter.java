package org.dromara.mes.msg.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 事件对象 mes_msg_day_matter
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_msg_day_matter")
public class MsgDayMatter extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 事件名称
     */
    private String dayName;

    /**
     * 事件时间
     */
    private Date dayTarget;

    /**
     * 事件类型（life, work, anniversary, birthday）
     */
    private String dayType;

    /**
     * 提醒周期（minutely, hourly, daily, weekly, monthly, yearly）
     */
    private String remindType;

    /**
     * 重复提醒
     */
    private String repeatFlag;

    /**
     * 通知状态（pending, notified, expired, disabled）
     */
    private String notifyStatus;

    /**
     * 下次通知时间
     */
    private Date nextNotifyTime;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 所属分组ID
     */
    private Long groupId;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;


}
