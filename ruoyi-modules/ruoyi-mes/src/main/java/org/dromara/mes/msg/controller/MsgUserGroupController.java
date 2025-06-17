package org.dromara.mes.msg.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.mes.system.excel.ExcelExportWrapper;
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
import org.dromara.mes.msg.domain.vo.MsgUserGroupVo;
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.mes.msg.service.IMsgUserGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 用户组
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgUserGroup")
public class MsgUserGroupController extends BaseController {

    private final IMsgUserGroupService msgUserGroupService;
    private final ExcelExportWrapper excelExportWrapper;

    /**
     * 查询用户组列表
     */
    @SaCheckPermission("msg:msgUserGroup:list")
    @GetMapping("/list")
    public TableDataInfo<MsgUserGroupVo> list(MsgUserGroupBo bo, PageQuery pageQuery) {
        return msgUserGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户组列表
     */
    @SaCheckPermission("msg:msgUserGroup:export")
    @Log(title = "用户组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgUserGroupBo bo, HttpServletResponse response) {
        List<MsgUserGroupVo> list = msgUserGroupService.queryList(bo);
        excelExportWrapper.exportWithSensitiveHandle(list, "用户组", MsgUserGroupVo.class, response);
    }

    /**
     * 获取用户组详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgUserGroup:query")
    @GetMapping("/{id}")
    public R<MsgUserGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        MsgUserGroupBo bo = new MsgUserGroupBo();
        bo.setId(id);
        List<MsgUserGroupVo> rows = msgUserGroupService.queryPageList(bo, new PageQuery(1, 1)).getRows();
        return rows.isEmpty() ? R.fail("数据不存在") : R.ok(rows.get(0));
    }

    /**
     * 新增用户组
     */
    @SaCheckPermission("msg:msgUserGroup:add")
    @Log(title = "用户组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgUserGroupBo bo) {
        return toAjax(msgUserGroupService.insertByBo(bo));
    }

    /**
     * 修改用户组
     */
    @SaCheckPermission("msg:msgUserGroup:edit")
    @Log(title = "用户组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgUserGroupBo bo) {
        return toAjax(msgUserGroupService.updateByBo(bo));
    }

    /**
     * 删除用户组
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgUserGroup:remove")
    @Log(title = "用户组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgUserGroupService.deleteWithValidByIds(List.of(ids), true));
    }
}
