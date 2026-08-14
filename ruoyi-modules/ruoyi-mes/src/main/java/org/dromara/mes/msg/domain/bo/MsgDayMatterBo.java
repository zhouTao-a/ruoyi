package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.mes.msg.support.NotifyTimeHelper;

import java.util.Date;

/**
 * 事件业务对象 mes_msg_day_matter
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgDayMatter.class, reverseConvertGenerate = false)
public class MsgDayMatterBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 255, message = "事件名称长度不能超过255", groups = { AddGroup.class, EditGroup.class })
    private String dayName;

    /**
     * 事件时间
     */
    @NotNull(message = "事件时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date dayTarget;

    /**
     *  时间类型（solar-公历, lunar-农历）
     */
    @NotNull(message = "时间类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dayLunar;

    /**
     * 事件类型（life, work, anniversary, birthday）
     */
    @NotBlank(message = "事件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dayType;

    /**
     * 提醒周期（minutely, hourly, daily, weekly, monthly, yearly）
     */
    @NotBlank(message = "提醒周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remindType;

    /**
     * 重复提醒
     */
    @NotBlank(message = "重复提醒不能为空", groups = { AddGroup.class, EditGroup.class })
    private String repeatFlag;

    /**
     * 通知状态（pending, notified, expired, disabled）
     */
    @NotBlank(message = "通知状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String notifyStatus;

    /**
     * 下次通知时间
     */
    private Date nextNotifyTime;


    /**
     * 下次通知时间
     */
    private Date notifyStartTime;

    /**
     * 下次通知时间
     */
    private Date notifyEndTime;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 按循环规则计算下次通知时间（无时刻事件落到当天 06:00）
     */
    public void calculateNextNotifyTime() {
        this.nextNotifyTime = NotifyTimeHelper.calculate(dayTarget, remindType, dayLunar);
    }




}
