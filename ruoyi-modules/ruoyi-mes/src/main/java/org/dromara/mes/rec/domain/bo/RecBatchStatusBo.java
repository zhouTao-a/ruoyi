package org.dromara.mes.rec.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量修改状态
 */
@Data
public class RecBatchStatusBo {

    /**
     * 主键列表
     */
    @NotEmpty(message = "请选择数据")
    private List<Long> ids;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空")
    private String status;
}
