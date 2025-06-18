package org.dromara.mes.msg.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 事件组对象 mes_msg_matter_group
 *
 * @author allen
 * @date 2025-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_msg_matter_group")
public class MsgMatterGroup extends TenantEntity {

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
    private Long matterId;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;


}
