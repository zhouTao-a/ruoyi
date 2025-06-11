package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgDayMatterUser;
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
 * 事件与用户关联视图对象 mes_msg_day_matter_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgDayMatterUser.class)
public class MsgDayMatterUserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 事件ID
     */
    @ExcelProperty(value = "事件ID")
    private Long dayMatterId;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;


}
