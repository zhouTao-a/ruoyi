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
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.service.IMsgDayMatterService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 事件
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgDayMatter")
public class MsgDayMatterController extends BaseController {

    private final IMsgDayMatterService msgDayMatterService;

    /**
     * 查询事件列表
     */
    @SaCheckPermission("msg:msgDayMatter:list")
    @GetMapping("/list")
    public TableDataInfo<MsgDayMatterVo> list(MsgDayMatterBo bo, PageQuery pageQuery) {
        return msgDayMatterService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出事件列表
     */
    @SaCheckPermission("msg:msgDayMatter:export")
    @Log(title = "事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgDayMatterBo bo, HttpServletResponse response) {
        List<MsgDayMatterVo> list = msgDayMatterService.queryList(bo);
        ExcelUtil.exportExcel(list, "事件", MsgDayMatterVo.class, response);
    }

    /**
     * 获取事件详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgDayMatter:query")
    @GetMapping("/{id}")
    public R<MsgDayMatterVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(msgDayMatterService.queryById(id));
    }

    /**
     * 新增事件
     */
    @SaCheckPermission("msg:msgDayMatter:add")
    @Log(title = "事件", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgDayMatterBo bo) {
        return toAjax(msgDayMatterService.insertByBo(bo));
    }

    /**
     * 修改事件
     */
    @SaCheckPermission("msg:msgDayMatter:edit")
    @Log(title = "事件", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgDayMatterBo bo) {
        return toAjax(msgDayMatterService.updateByBo(bo));
    }

    /**
     * 删除事件
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgDayMatter:remove")
    @Log(title = "事件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgDayMatterService.deleteWithValidByIds(List.of(ids), true));
    }
}
