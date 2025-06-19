package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgUserGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

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
    private Long userId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private List<String> userIdList;

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 辈分差
     */
    @Min(value = -100, message = "辈分差不能小于-100", groups = { AddGroup.class, EditGroup.class })
    @Max(value = 100, message = "辈分差不能大于100", groups = { AddGroup.class, EditGroup.class })
    private Long relativeGenerationDiff = 0L;

    /**
     * 亲缘关系（spouse, close, distant, friend, stranger）
     */
    private String kinshipLevel;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 用户名
     */
    private String userName;


}
