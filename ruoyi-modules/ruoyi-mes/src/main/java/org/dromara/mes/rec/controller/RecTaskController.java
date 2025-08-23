package org.dromara.mes.rec.controller;

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
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.domain.bo.RecTaskBo;
import org.dromara.mes.rec.service.IRecTaskService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 任务
 *
 * @author allen
 * @date 2025-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recTask")
public class RecTaskController extends BaseController {

    private final IRecTaskService recTaskService;

    /**
     * 查询任务列表
     */
    @SaCheckPermission("rec:recTask:list")
    @GetMapping("/list")
    public TableDataInfo<RecTaskVo> list(RecTaskBo bo, PageQuery pageQuery) {
        return recTaskService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出任务列表
     */
    @SaCheckPermission("rec:recTask:export")
    @Log(title = "任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecTaskBo bo, HttpServletResponse response) {
        List<RecTaskVo> list = recTaskService.queryList(bo);
        ExcelUtil.exportExcel(list, "任务", RecTaskVo.class, response);
    }

    /**
     * 获取任务详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("rec:recTask:query")
    @GetMapping("/{id}")
    public R<RecTaskVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recTaskService.queryById(id));
    }

    /**
     * 新增任务
     */
    @SaCheckPermission("rec:recTask:add")
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecTaskBo bo) {
        return toAjax(recTaskService.insertByBo(bo));
    }

    /**
     * 修改任务
     */
    @SaCheckPermission("rec:recTask:edit")
    @Log(title = "任务", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecTaskBo bo) {
        return toAjax(recTaskService.updateByBo(bo));
    }

    /**
     * 删除任务
     *
     * @param ids 主键串
     */
    @SaCheckPermission("rec:recTask:remove")
    @Log(title = "任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recTaskService.deleteWithValidByIds(List.of(ids), true));
    }
}
