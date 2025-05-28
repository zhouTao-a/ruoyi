package org.dromara.mes.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * IP白名单对象 mes_ip_white_list
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_ip_white_list")
public class IpWhiteList extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * IP地址或CIDR网段
     */
    private String ipAddress;

    /**
     * 备注说明
     */
    private String description;

    /**
     * 状态：1-有效，0-无效
     */
    private Integer status;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 备注
     */
    private String remark;


}
