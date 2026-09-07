package org.dromara.mes.rec.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.mes.rec.domain.RecIntrospect;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 自省主题视图
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecIntrospect.class)
public class RecIntrospectVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @ExcelProperty(value = "标题")
    private String title;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "排序")
    private Long sortOrder;

    private Long userId;

    /**
     * 当日明细，仅当前生效查询时填充
     */
    private List<RecIntrospectItemVo> items;
}
