package org.dromara.mes.rec.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.mes.rec.domain.bo.RecGoalBo;
import org.dromara.mes.rec.domain.bo.RecBatchStatusBo;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.service.IRecGoalService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目标
 *
 * @author allen
 * @date 2025-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recGoal")
public class RecGoalController extends BaseController {

    private final IRecGoalService recGoalService;

    /**
     * 查询目标列表（展示页登录即可，不校验菜单权限）
     */
    @GetMapping(value = "/list")
    public TableDataInfo<RecGoalVo> list(RecGoalBo bo, PageQuery pageQuery) {
        return recGoalService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出目标列表
     */
    @SaCheckPermission("rec:recGoal:export")
    @Log(title = "目标", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecGoalBo bo, HttpServletResponse response) {
        List<RecGoalVo> list = recGoalService.queryPageList(bo, new PageQuery()).getRows();
        ExcelUtil.exportExcel(list, "目标", RecGoalVo.class, response);
    }

    /**
     * 获取目标详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("rec:recGoal:query")
    @GetMapping("/{id}")
    public R<RecGoalVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recGoalService.queryById(id));
    }

    /**
     * 新增目标
     */
    @SaCheckPermission("rec:recGoal:add")
    @Log(title = "目标", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecGoalBo bo) {
        return toAjax(recGoalService.insertByBo(bo));
    }

    /**
     * 修改目标
     */
    @SaCheckPermission("rec:recGoal:edit")
    @Log(title = "目标", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecGoalBo bo) {
        return toAjax(recGoalService.updateByBo(bo));
    }

    /**
     * 批量修改目标状态
     */
    @SaCheckPermission("rec:recGoal:edit")
    @Log(title = "目标", businessType = BusinessType.UPDATE)
    @PutMapping("/batchStatus")
    public R<Void> batchStatus(@Validated @RequestBody RecBatchStatusBo bo) {
        return toAjax(recGoalService.updateStatusByIds(bo.getIds(), bo.getStatus()));
    }

    /**
     * 删除目标
     *
     * @param ids 主键串
     */
    @SaCheckPermission("rec:recGoal:remove")
    @Log(title = "目标", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recGoalService.deleteWithValidByIds(List.of(ids), true));
    }
}
