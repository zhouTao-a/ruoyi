package org.dromara.mes.rec.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.mes.rec.domain.RecIntrospectItem;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 自省明细业务对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecIntrospectItem.class, reverseConvertGenerate = false)
public class RecIntrospectItemBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    @NotNull(message = "主题不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long introspectId;

    @NotNull(message = "日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date occurDate;

    @NotBlank(message = "情况说明不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    private Long sortOrder;

    private Long userId;

    /** 查询用：发生日期起（含） */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date occurDateBegin;

    /** 查询用：发生日期止（不含） */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date occurDateEnd;
}
