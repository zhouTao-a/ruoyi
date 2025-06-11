package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgUserGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 用户组业务对象 mes_msg_user_group
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgUserGroup.class, reverseConvertGenerate = false)
public class MsgUserGroupBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 代际差
     */
    private Long relativeGenerationDiff;

    /**
     * 亲缘关系（close, distant, friend, stranger）
     */
    @NotBlank(message = "亲缘关系（close, distant, friend, stranger）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kinshipLevel;


}
