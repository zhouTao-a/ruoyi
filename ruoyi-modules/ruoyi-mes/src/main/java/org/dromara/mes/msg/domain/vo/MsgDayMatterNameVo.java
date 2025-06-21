package org.dromara.mes.msg.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 事件视图对象 mes_msg_day_matter
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
public class MsgDayMatterNameVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 事件名称
     */
    private String dayName;

}
