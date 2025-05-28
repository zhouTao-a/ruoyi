package org.dromara.mes.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.mes.system.domain.IpWhiteList;

import java.io.Serial;
import java.io.Serializable;


/**
 * IP白名单视图对象 mes_ip_white_list
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = IpWhiteList.class)
public class IpWhiteListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * IP地址或CIDR网段
     */
    @ExcelProperty(value = "IP地址或CIDR网段")
    private String ipAddress;

    /**
     * 备注说明
     */
    @ExcelProperty(value = "备注说明")
    private String description;

    /**
     * 状态：1-有效，0-无效
     */
    @ExcelProperty(value = "状态：1-有效，0-无效")
    private Integer status;

    /**
     * 部门ID
     */
    @ExcelProperty(value = "部门ID")
    private Long deptId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
