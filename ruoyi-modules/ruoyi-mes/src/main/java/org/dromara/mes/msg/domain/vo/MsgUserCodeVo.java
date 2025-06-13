package org.dromara.mes.msg.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 用户视图对象 mes_msg_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
public class MsgUserCodeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String userName;

    /**
     * 用户编码
     */
    @ExcelProperty(value = "用户编码")
    private String userCode;

}
