package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.enums.RemindTypeEnum;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
     * 所属用户ID
     */
    private Long userId;

    // 自动计算下次通知时间
    public void calculateNextNotifyTime() {
        if (dayTarget == null || remindType == null || remindType.isBlank()) {
            return;
        }

        RemindTypeEnum typeEnum;
        try {
            typeEnum = RemindTypeEnum.fromCode(remindType); // 根据字符串获取枚举
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("提醒周期无效: " + remindType);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = LocalDateTime.ofInstant(dayTarget.toInstant(), ZoneId.systemDefault());

        while (!next.isAfter(now)) {
            next = switch (typeEnum) {
                case MINUTELY -> next.plusMinutes(1);
                case HOURLY -> next.plusHours(1);
                case DAILY -> next.plusDays(1);
                case WEEKLY -> next.plusWeeks(1);
                case MONTHLY -> next.plusMonths(1);
                case YEARLY -> next.plusYears(1);
            };
        }

        this.nextNotifyTime = Date.from(next.atZone(ZoneId.systemDefault()).toInstant());
    }


}
