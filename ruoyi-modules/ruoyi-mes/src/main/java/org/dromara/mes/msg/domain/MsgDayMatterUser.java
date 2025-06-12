package org.dromara.mes.msg.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 事件与用户关联对象 mes_msg_day_matter_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_msg_day_matter_user")
public class MsgDayMatterUser extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
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
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;


}
