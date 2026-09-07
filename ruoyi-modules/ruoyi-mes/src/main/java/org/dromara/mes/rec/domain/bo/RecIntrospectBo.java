package org.dromara.mes.rec.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.mes.rec.domain.RecIntrospect;

/**
 * 自省主题业务对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecIntrospect.class, reverseConvertGenerate = false)
public class RecIntrospectBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 255, message = "标题长度不能超过255个字符")
    private String title;

    /**
     * active 生效 / inactive 失效
     */
    private String status;

    private Long sortOrder;

    private Long userId;
}
