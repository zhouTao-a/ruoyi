package org.dromara.mes.msg.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.service.IMsgUserService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 用户
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgUser")
public class MsgUserController extends BaseController {

    private final IMsgUserService msgUserService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("msg:msgUser:list")
    @GetMapping("/list")
    public TableDataInfo<MsgUserVo> list(MsgUserBo bo, PageQuery pageQuery) {
        return msgUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("msg:msgUser:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgUserBo bo, HttpServletResponse response) {
        List<MsgUserVo> list = msgUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", MsgUserVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgUser:query")
    @GetMapping("/{id}")
    public R<MsgUserVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(msgUserService.queryById(id));
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("msg:msgUser:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgUserBo bo) {
        return toAjax(msgUserService.insertByBo(bo));
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("msg:msgUser:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgUserBo bo) {
        return toAjax(msgUserService.updateByBo(bo));
    }

    /**
     * 删除用户
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgUser:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgUserService.deleteWithValidByIds(List.of(ids), true));
    }
}
