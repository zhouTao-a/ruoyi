package org.dromara.mes.msg.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import org.dromara.mes.msg.domain.MsgDayMatter;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.mes.msg.support.NotifyTimeHelper;

import java.io.Serial;
import java.io.Serializable;


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
     * 按循环规则计算下次通知时间（无时刻事件落到当天 06:00）
     */
    public void calculateNextNotifyTime() {
        this.nextNotifyTime = NotifyTimeHelper.calculate(dayTarget, remindType, dayLunar);
    }


}
