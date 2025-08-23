package org.dromara.mes.rec.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 报告对象 mes_rec_report
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_rec_report")
public class RecReport extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 类型
     */
    private String reportType;

    /**
     * 日期
     */
    private Date reportDate;

    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;


}
