package org.dromara.mes.rec.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 感想对象 mes_rec_reflection
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_rec_reflection")
public class RecReflection extends TenantEntity {

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
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 来源链接
     */
    private String sourceLink;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;


}
