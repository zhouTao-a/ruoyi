package org.dromara.mes.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.mes.system.domain.IpWhiteList;

/**
 * IP白名单业务对象 mes_ip_white_list
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = IpWhiteList.class, reverseConvertGenerate = false)
public class IpWhiteListBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * IP地址或CIDR网段
     */
    @NotBlank(message = "IP地址或CIDR网段不能为空", groups = { AddGroup.class, EditGroup.class })
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
