package org.dromara.mes.msg.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 用户
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
public class MsgUserCodeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户编码
     */
    private String userCode;

}
