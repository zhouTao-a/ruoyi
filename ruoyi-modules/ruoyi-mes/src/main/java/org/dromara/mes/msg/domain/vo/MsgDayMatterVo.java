package org.dromara.mes.msg.domain.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import org.dromara.mes.enums.RemindTypeEnum;
import org.dromara.mes.msg.domain.MsgDayMatter;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.mes.utils.LunarSolarUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;


/**
 * 事件视图对象 mes_msg_day_matter
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgDayMatter.class)
public class MsgDayMatterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 事件名称
     */
    @ExcelProperty(value = "事件名称")
    private String dayName;

    /**
     * 事件时间
     */
    @ExcelProperty(value = "事件时间")
    @ColumnWidth(20)
    private Date dayTarget;


    /**
     *  时间类型（solar-公历, lunar-农历）
     */
    @ExcelProperty(value = "时间类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "day_lunar")
    private String dayLunar;

    /**
     * 事件类型（life, work, anniversary, birthday）
     */
    @ExcelProperty(value = "事件类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "day_type")
    private String dayType;

    /**
     * 提醒周期（minutely, hourly, daily, weekly, monthly, yearly）
     */
    @ExcelProperty(value = "提醒周期", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "remind_type")
    private String remindType;

    /**
     * 重复提醒
     */
    @ExcelProperty(value = "重复提醒", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "whether_flag")
    private String repeatFlag;

    /**
     * 通知状态（pending, notified, expired, disabled）
     */
    @ExcelProperty(value = "通知状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "notify_status")
    private String notifyStatus;

    /**
     * 下次通知时间
     */
    @ExcelProperty(value = "通知时间")
    @ColumnWidth(20)
    private Date nextNotifyTime;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户编码
     */
    @ExcelProperty(value = "用户编码")
    private String userCode;

    /**
     * 此方法从 MsgDayMatterBo 复制而来，如有修改需同步其他类
     */
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
                case YEARLY -> {
                    // 判断是否是农历
                    if ("lunar".equalsIgnoreCase(dayLunar)) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dayTarget);
                        int lunarMonth = cal.get(Calendar.MONTH) + 1;
                        int lunarDay = cal.get(Calendar.DAY_OF_MONTH);
                        int tryYear = LocalDate.now().getYear(); // 从当前年开始尝试

                        LocalDateTime nextLunarDate;

                        while (true) {
                            Set<String> solarDates = LunarSolarUtils.lunarToSolarTryBoth(tryYear, lunarMonth, lunarDay);
                            Optional<LocalDateTime> future = solarDates.stream()
                                .map(s -> LocalDateTime.parse(s + "T00:00:00"))
                                .sorted() // 确保从早到晚
                                .filter(d -> d.isAfter(now))
                                .findFirst();

                            if (future.isPresent()) {
                                nextLunarDate = future.get();
                                break;
                            }

                            tryYear++; // 当前年不满足，继续下一年
                        }

                        yield nextLunarDate;
                    } else {
                        yield next.plusYears(1);
                    }
                }
            };
        }

        this.nextNotifyTime = Date.from(next.atZone(ZoneId.systemDefault()).toInstant());
    }
}
