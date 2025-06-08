package org.dromara.mes.msg.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.mes.msg.domain.MsgDayMatter;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



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
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 事件名称
     */
    @ExcelProperty(value = "事件名称")
    private String dayName;

    /**
     * 事件目标时间（含时分）
     */
    @ExcelProperty(value = "事件目标时间", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "含=时分")
    private Date dayTarget;

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
     * 是否重复提醒（T/F）
     */
    @ExcelProperty(value = "是否重复提醒", converter = ExcelDictConvert.class)
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
    @ExcelProperty(value = "下次通知时间")
    private Date nextNotifyTime;

    /**
     * 所属用户ID
     */
    @ExcelProperty(value = "所属用户ID")
    private Long userId;

    /**
     * 所属分组ID
     */
    @ExcelProperty(value = "所属分组ID")
    private Long groupId;


}
