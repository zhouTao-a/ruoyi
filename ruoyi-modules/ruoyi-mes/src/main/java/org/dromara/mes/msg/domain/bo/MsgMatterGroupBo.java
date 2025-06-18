package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 事件组业务对象 mes_msg_matter_group
 *
 * @author allen
 * @date 2025-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgMatterGroup.class, reverseConvertGenerate = false)
public class MsgMatterGroupBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 事件ID
     */
    @NotNull(message = "事件ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long matterId;

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;


}
