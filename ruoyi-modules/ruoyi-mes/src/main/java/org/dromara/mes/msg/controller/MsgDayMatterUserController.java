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
import org.dromara.mes.msg.domain.vo.MsgDayMatterUserVo;
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.mes.msg.service.IMsgDayMatterUserService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 事件与用户关联
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgDayMatterUser")
public class MsgDayMatterUserController extends BaseController {

    private final IMsgDayMatterUserService msgDayMatterUserService;
    private final ExcelExportWrapper excelExportWrapper;

    /**
     * 查询事件与用户关联列表
     */
    @SaCheckPermission("msg:msgDayMatterUser:list")
    @GetMapping("/list")
    public TableDataInfo<MsgDayMatterUserVo> list(MsgDayMatterUserBo bo, PageQuery pageQuery) {
        return msgDayMatterUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出事件与用户关联列表
     */
    @SaCheckPermission("msg:msgDayMatterUser:export")
    @Log(title = "事件与用户关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgDayMatterUserBo bo, HttpServletResponse response) {
        List<MsgDayMatterUserVo> list = msgDayMatterUserService.queryList(bo);
        excelExportWrapper.exportWithSensitiveHandle(list, "事件与用户关联", MsgDayMatterUserVo.class, response);
    }

    /**
     * 获取事件与用户关联详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgDayMatterUser:query")
    @GetMapping("/{id}")
    public R<MsgDayMatterUserVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        MsgDayMatterUserBo bo = new MsgDayMatterUserBo();
        bo.setId(id);
        List<MsgDayMatterUserVo> rows = msgDayMatterUserService.queryPageList(bo, new PageQuery(1, 1)).getRows();
        return rows.isEmpty() ? R.fail("数据不存在") : R.ok(rows.get(0));
    }

    /**
     * 新增事件与用户关联
     */
    @SaCheckPermission("msg:msgDayMatterUser:add")
    @Log(title = "事件与用户关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgDayMatterUserBo bo) {
        return toAjax(msgDayMatterUserService.insertByBo(bo));
    }

    /**
     * 修改事件与用户关联
     */
    @SaCheckPermission("msg:msgDayMatterUser:edit")
    @Log(title = "事件与用户关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgDayMatterUserBo bo) {
        return toAjax(msgDayMatterUserService.updateByBo(bo));
    }

    /**
     * 删除事件与用户关联
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgDayMatterUser:remove")
    @Log(title = "事件与用户关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgDayMatterUserService.deleteWithValidByIds(List.of(ids), true));
    }
}
