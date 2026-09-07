package org.dromara.mes.rec.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.mes.rec.domain.RecIntrospectItem;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 自省明细视图
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecIntrospectItem.class)
public class RecIntrospectItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long introspectId;

    /**
     * 所属主题标题，邮件昨日回顾用
     */
    private String introspectTitle;

    @ExcelProperty(value = "日期")
    private Date occurDate;

    @ExcelProperty(value = "情况")
    private String content;

    private Long sortOrder;

    private Long userId;
}
