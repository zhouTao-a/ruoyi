package org.dromara.mes.msg.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.mes.utils.SensitiveDataUtils;
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
import org.dromara.mes.msg.domain.vo.MsgGroupVo;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.mes.msg.service.IMsgGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 分组信息
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgGroup")
public class MsgGroupController extends BaseController {

    private final IMsgGroupService msgGroupService;

    /**
     * 查询分组信息列表
     */
    @SaCheckPermission("msg:msgGroup:list")
    @GetMapping("/list")
    public TableDataInfo<MsgGroupVo> list(MsgGroupBo bo, PageQuery pageQuery) {
        return msgGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分组信息列表
     */
    @SaCheckPermission("msg:msgGroup:export")
    @Log(title = "分组信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgGroupBo bo, HttpServletResponse response) {
        List<MsgGroupVo> list = msgGroupService.queryList(bo);
        List<MsgGroupVo> handle = SensitiveDataUtils.handle(list);
        ExcelUtil.exportExcel(handle, "分组信息", MsgGroupVo.class, response);
    }

    /**
     * 获取分组信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgGroup:query")
    @GetMapping("/{id}")
    public R<MsgGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        MsgGroupBo msgGroupBo = new MsgGroupBo();
        msgGroupBo.setId(id);
        TableDataInfo<MsgGroupVo> msgGroupVoTableDataInfo = msgGroupService.queryPageList(msgGroupBo, new PageQuery(1, 1));
        return R.ok(msgGroupVoTableDataInfo.getRows().get(0));
    }

    /**
     * 新增分组信息
     */
    @SaCheckPermission("msg:msgGroup:add")
    @Log(title = "分组信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgGroupBo bo) {
        return toAjax(msgGroupService.insertByBo(bo));
    }

    /**
     * 修改分组信息
     */
    @SaCheckPermission("msg:msgGroup:edit")
    @Log(title = "分组信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgGroupBo bo) {
        return toAjax(msgGroupService.updateByBo(bo));
    }

    /**
     * 删除分组信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgGroup:remove")
    @Log(title = "分组信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgGroupService.deleteWithValidByIds(List.of(ids), true));
    }
}
