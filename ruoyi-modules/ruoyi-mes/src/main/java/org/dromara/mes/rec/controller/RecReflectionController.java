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
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.bo.RecReflectionBo;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 感想
 *
 * @author allen
 * @date 2025-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recReflection")
public class RecReflectionController extends BaseController {

    private final IRecReflectionService recReflectionService;

    /**
     * 查询感想列表
     */
    @SaCheckPermission("rec:recReflection:list")
    @GetMapping("/list")
    public TableDataInfo<RecReflectionVo> list(RecReflectionBo bo, PageQuery pageQuery) {
        return recReflectionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出感想列表
     */
    @SaCheckPermission("rec:recReflection:export")
    @Log(title = "感想", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecReflectionBo bo, HttpServletResponse response) {
        List<RecReflectionVo> list = recReflectionService.queryList(bo);
        ExcelUtil.exportExcel(list, "感想", RecReflectionVo.class, response);
    }

    /**
     * 获取感想详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("rec:recReflection:query")
    @GetMapping("/{id}")
    public R<RecReflectionVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recReflectionService.queryById(id));
    }

    /**
     * 新增感想
     */
    @SaCheckPermission("rec:recReflection:add")
    @Log(title = "感想", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecReflectionBo bo) {
        return toAjax(recReflectionService.insertByBo(bo));
    }

    /**
     * 修改感想
     */
    @SaCheckPermission("rec:recReflection:edit")
    @Log(title = "感想", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecReflectionBo bo) {
        return toAjax(recReflectionService.updateByBo(bo));
    }

    /**
     * 删除感想
     *
     * @param ids 主键串
     */
    @SaCheckPermission("rec:recReflection:remove")
    @Log(title = "感想", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recReflectionService.deleteWithValidByIds(List.of(ids), true));
    }
}
