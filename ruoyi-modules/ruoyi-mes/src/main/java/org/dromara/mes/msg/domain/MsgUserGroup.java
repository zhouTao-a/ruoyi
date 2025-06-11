package org.dromara.mes.msg.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户组对象 mes_msg_user_group
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_msg_user_group")
public class MsgUserGroup extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 代际差
     */
    private Long relativeGenerationDiff;

    /**
     * 亲缘关系（close, distant, friend, stranger）
     */
    private String kinshipLevel;

    /**
     * 逻辑删除标志（T/F）
     */
    private String deletedFlag;


}
