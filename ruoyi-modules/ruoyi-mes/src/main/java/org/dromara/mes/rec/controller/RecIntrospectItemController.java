package org.dromara.mes.rec.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.mes.rec.domain.bo.RecIntrospectItemBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectItemVo;
import org.dromara.mes.rec.service.IRecIntrospectItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自省按天明细
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/recIntrospectItem")
public class RecIntrospectItemController extends BaseController {

    private final IRecIntrospectItemService recIntrospectItemService;

    /**
     * 按主题和日期查询明细
     */
    @SaCheckPermission("rec:recIntrospect:list")
    @GetMapping("/list")
    public TableDataInfo<RecIntrospectItemVo> list(RecIntrospectItemBo bo, PageQuery pageQuery) {
        return recIntrospectItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 明细详情
     */
    @SaCheckPermission("rec:recIntrospect:query")
    @GetMapping("/{id}")
    public R<RecIntrospectItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(recIntrospectItemService.queryById(id));
    }

    /**
     * 新增当天情况
     */
    @SaCheckPermission("rec:recIntrospect:add")
    @Log(title = "自省明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecIntrospectItemBo bo) {
        return toAjax(recIntrospectItemService.insertByBo(bo));
    }

    /**
     * 修改情况说明
     */
    @SaCheckPermission("rec:recIntrospect:edit")
    @Log(title = "自省明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecIntrospectItemBo bo) {
        return toAjax(recIntrospectItemService.updateByBo(bo));
    }

    /**
     * 删除明细
     */
    @SaCheckPermission("rec:recIntrospect:remove")
    @Log(title = "自省明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(recIntrospectItemService.deleteWithValidByIds(List.of(ids), true));
    }
}
