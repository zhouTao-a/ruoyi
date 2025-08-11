package org.dromara.mes.rec.domain.vo;

import org.dromara.mes.rec.domain.RecReflection;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 感想视图对象 mes_rec_reflection
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecReflection.class)
public class RecReflectionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 来源类型
     */
    @ExcelProperty(value = "来源类型")
    private String sourceType;

    /**
     * 来源名称
     */
    @ExcelProperty(value = "来源名称")
    private String sourceName;

    /**
     * 来源链接
     */
    @ExcelProperty(value = "来源链接")
    private String sourceLink;

    /**
     * 用户
     */
    private Long userId;


}
