package org.dromara.mes.msg.domain.vo;

import java.util.Date;

import org.dromara.common.sensitive.annotation.Sensitive;
import org.dromara.common.sensitive.core.SensitiveStrategy;
import org.dromara.mes.msg.domain.MsgUser;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 用户视图对象 mes_msg_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgUser.class)
public class MsgUserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String userName;

    /**
     * 用户编码
     */
    @ExcelProperty(value = "用户编码")
    private String userCode;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    private String gender;

    /**
     * 父亲
     */
    @ExcelProperty(value = "父亲")
    private String fatherName;

    /**
     * 父亲编码
     */
    @ExcelProperty(value = "父亲编码")
    private String fatherCode;

    /**
     * 母亲名称
     */
    @ExcelProperty(value = "母亲")
    private String motherName;

    /**
     * 母亲编码
     */
    @ExcelProperty(value = "母亲编码")
    private String motherCode;

    /**
     * 配偶名称
     */
    @ExcelProperty(value = "配偶")
    private String spouseName;

    /**
     * 配偶编码
     */
    @ExcelProperty(value = "配偶编码")
    private String spouseCode;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String idCard;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phoneNumber;

    /**
     * 出生日期
     */
    @ExcelProperty(value = "出生日期")
    private Date birthday;

    /**
     * 农历出生日期
     */
    @ExcelProperty(value = "农历出生日期")
    private Date lunarBirthday;

    /**
     * 邮箱地址
     */
    @ExcelProperty(value = "邮箱地址")
    private String email;

    /**
     * 短信通知
     */
    private String smsNotifyFlag;

    /**
     * 邮箱通知
     */
    @ExcelProperty(value = "邮箱通知", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "whether_flag")
    private String emailNotifyFlag;

    /**
     * 父级ID
     */
    private Long fatherId;

    /**
     * 母亲ID
     */
    private Long motherId;

    /**
     * 配偶ID
     */
    private Long spouseId;


}
