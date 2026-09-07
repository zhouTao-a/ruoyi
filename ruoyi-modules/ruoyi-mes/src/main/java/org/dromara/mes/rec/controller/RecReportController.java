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
import org.dromara.mes.rec.domain.vo.RecReportVo;
import org.dromara.mes.rec.domain.bo.RecReportBo;
import org.dromara.mes.rec.service.IRecReportService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 报告
 *
 * @author allen
 * @date 2025-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recReport")
public class RecReportController extends BaseController {

    private final IRecReportService recReportService;

    /**
     * 查询报告列表（展示页登录即可，不校验菜单权限）
     */
    @GetMapping("/list")
    public TableDataInfo<RecReportVo> list(RecReportBo bo, PageQuery pageQuery) {
        return recReportService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出报告列表
     */
    @SaCheckPermission("rec:recReport:export")
    @Log(title = "报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecReportBo bo, HttpServletResponse response) {
        List<RecReportVo> list = recReportService.queryList(bo);
        ExcelUtil.exportExcel(list, "报告", RecReportVo.class, response);
    }

    /**
     * 获取报告详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("rec:recReport:query")
    @GetMapping("/{id}")
    public R<RecReportVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recReportService.queryById(id));
    }

    /**
     * 新增报告
     */
    @SaCheckPermission("rec:recReport:add")
    @Log(title = "报告", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecReportBo bo) {
        return toAjax(recReportService.insertByBo(bo));
    }

    /**
     * 修改报告
     */
    @SaCheckPermission("rec:recReport:edit")
    @Log(title = "报告", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecReportBo bo) {
        return toAjax(recReportService.updateByBo(bo));
    }

    /**
     * 删除报告
     *
     * @param ids 主键串
     */
    @SaCheckPermission("rec:recReport:remove")
    @Log(title = "报告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recReportService.deleteWithValidByIds(List.of(ids), true));
    }
}
