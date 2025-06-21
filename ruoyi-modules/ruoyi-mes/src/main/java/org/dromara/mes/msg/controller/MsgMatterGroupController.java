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
import org.dromara.mes.msg.domain.vo.MsgMatterGroupVo;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.mes.msg.service.IMsgMatterGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 事件组
 *
 * @author allen
 * @date 2025-06-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/msgMatterGroup")
public class MsgMatterGroupController extends BaseController {

    private final IMsgMatterGroupService msgMatterGroupService;

    /**
     * 查询事件组列表
     */
    @SaCheckPermission("msg:msgMatterGroup:list")
    @GetMapping("/list")
    public TableDataInfo<MsgMatterGroupVo> list(MsgMatterGroupBo bo, PageQuery pageQuery) {
        return msgMatterGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出事件组列表
     */
    @SaCheckPermission("msg:msgMatterGroup:export")
    @Log(title = "事件组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MsgMatterGroupBo bo, HttpServletResponse response) {
        List<MsgMatterGroupVo> list = msgMatterGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "事件组", MsgMatterGroupVo.class, response);
    }

    /**
     * 获取事件组详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("msg:msgMatterGroup:query")
    @GetMapping("/{id}")
    public R<MsgMatterGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        MsgMatterGroupBo bo = new MsgMatterGroupBo();
        bo.setId(id);
        List<MsgMatterGroupVo> rows = msgMatterGroupService.queryPageList(bo, new PageQuery(1, 1)).getRows();
        return rows.isEmpty() ? R.fail("数据不存在") : R.ok(rows.get(0));
    }

    /**
     * 新增事件组
     */
    @SaCheckPermission("msg:msgMatterGroup:add")
    @Log(title = "事件组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MsgMatterGroupBo bo) {
        return toAjax(msgMatterGroupService.insertByBo(bo));
    }

    /**
     * 修改事件组
     */
    @SaCheckPermission("msg:msgMatterGroup:edit")
    @Log(title = "事件组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MsgMatterGroupBo bo) {
        return toAjax(msgMatterGroupService.updateByBo(bo));
    }

    /**
     * 删除事件组
     *
     * @param ids 主键串
     */
    @SaCheckPermission("msg:msgMatterGroup:remove")
    @Log(title = "事件组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(msgMatterGroupService.deleteWithValidByIds(List.of(ids), true));
    }
}
