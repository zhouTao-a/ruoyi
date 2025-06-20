package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgDayMatterUser;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


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
    private Long id;

    /**
     * 事件ID
     */
    private Long dayMatterId;

    /**
     * 用户ID
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
     * 事件名称
     */
    @ExcelProperty(value = "事件名称")
    private String dayName;


}
