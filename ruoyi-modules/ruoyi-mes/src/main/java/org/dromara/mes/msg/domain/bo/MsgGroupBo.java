package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 分组信息业务对象 mes_msg_group
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgGroup.class, reverseConvertGenerate = false)
public class MsgGroupBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 20, message = "分组名称长度不能超过20个字符", groups = { AddGroup.class, EditGroup.class })
    private String groupName;

    /**
     * 分组编码
     */
    @NotBlank(message = "分组编码不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 20, message = "分组编码长度不能超过20个字符", groups = { AddGroup.class, EditGroup.class })
    private String groupCode;

    /**
     * 默认参考用户ID
     */
    private Long defaultTargetUserId;


}
