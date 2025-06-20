package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgDayMatterUser;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 事件与用户关联业务对象 mes_msg_day_matter_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgDayMatterUser.class, reverseConvertGenerate = false)
public class MsgDayMatterUserBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 事件ID
     */
    @NotNull(message = "事件ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long dayMatterId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户ID列表
     */
    private List<String> userIdList;


}
