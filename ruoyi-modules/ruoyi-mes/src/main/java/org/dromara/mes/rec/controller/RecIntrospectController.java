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
import org.dromara.mes.rec.domain.bo.RecBatchStatusBo;
import org.dromara.mes.rec.domain.bo.RecIntrospectBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectVo;
import org.dromara.mes.rec.service.IRecIntrospectService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自省主题
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recIntrospect")
public class RecIntrospectController extends BaseController {

    private final IRecIntrospectService recIntrospectService;

    /**
     * 查询自省主题列表（展示页登录即可，不校验菜单权限）
     */
    @GetMapping("/list")
    public TableDataInfo<RecIntrospectVo> list(RecIntrospectBo bo, PageQuery pageQuery) {
        return recIntrospectService.queryPageList(bo, pageQuery);
    }

    /**
     * 当前生效主题及今日明细（展示页只读，登录即可）
     */
    @GetMapping("/current")
    public R<RecIntrospectVo> current() {
        return R.ok(recIntrospectService.queryCurrent());
    }

    /**
     * 导出自省主题
     */
    @SaCheckPermission("rec:recIntrospect:export")
    @Log(title = "自省", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecIntrospectBo bo, HttpServletResponse response) {
        List<RecIntrospectVo> list = recIntrospectService.queryList(bo);
        ExcelUtil.exportExcel(list, "自省", RecIntrospectVo.class, response);
    }

    /**
     * 获取自省主题详情
     */
    @SaCheckPermission("rec:recIntrospect:query")
    @GetMapping("/{id}")
    public R<RecIntrospectVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(recIntrospectService.queryById(id));
    }

    /**
     * 新增自省主题
     */
    @SaCheckPermission("rec:recIntrospect:add")
    @Log(title = "自省", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecIntrospectBo bo) {
        return toAjax(recIntrospectService.insertByBo(bo));
    }

    /**
     * 修改自省主题
     */
    @SaCheckPermission("rec:recIntrospect:edit")
    @Log(title = "自省", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecIntrospectBo bo) {
        return toAjax(recIntrospectService.updateByBo(bo));
    }

    /**
     * 修改生效状态；生效时其它主题自动失效
     */
    @SaCheckPermission("rec:recIntrospect:edit")
    @Log(title = "自省", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<Void> updateStatus(@Validated @RequestBody RecBatchStatusBo bo) {
        if (bo.getIds() == null || bo.getIds().size() != 1) {
            return R.fail("请选择一条自省主题");
        }
        return toAjax(recIntrospectService.updateStatusById(bo.getIds().get(0), bo.getStatus()));
    }

    /**
     * 删除自省主题
     */
    @SaCheckPermission("rec:recIntrospect:remove")
    @Log(title = "自省", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(recIntrospectService.deleteWithValidByIds(List.of(ids), true));
    }
}
