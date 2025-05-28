package org.dromara.mes.system.controller;

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
import org.dromara.mes.system.domain.bo.IpWhiteListBo;
import org.dromara.mes.system.domain.vo.IpWhiteListVo;
import org.dromara.mes.system.service.IIpWhiteListService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * IP白名单
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/ipWhiteList")
public class IpWhiteListController extends BaseController {

    private final IIpWhiteListService ipWhiteListService;

    /**
     * 查询IP白名单列表
     */
    @SaCheckPermission("system:ipWhiteList:list")
    @GetMapping("/list")
    public TableDataInfo<IpWhiteListVo> list(IpWhiteListBo bo, PageQuery pageQuery) {
        return ipWhiteListService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出IP白名单列表
     */
    @SaCheckPermission("system:ipWhiteList:export")
    @Log(title = "IP白名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(IpWhiteListBo bo, HttpServletResponse response) {
        List<IpWhiteListVo> list = ipWhiteListService.queryList(bo);
        ExcelUtil.exportExcel(list, "IP白名单", IpWhiteListVo.class, response);
    }

    /**
     * 获取IP白名单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:ipWhiteList:query")
    @GetMapping("/{id}")
    public R<IpWhiteListVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(ipWhiteListService.queryById(id));
    }

    /**
     * 新增IP白名单
     */
    @SaCheckPermission("system:ipWhiteList:add")
    @Log(title = "IP白名单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody IpWhiteListBo bo) {
        return toAjax(ipWhiteListService.insertByBo(bo));
    }

    /**
     * 修改IP白名单
     */
    @SaCheckPermission("system:ipWhiteList:edit")
    @Log(title = "IP白名单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody IpWhiteListBo bo) {
        return toAjax(ipWhiteListService.updateByBo(bo));
    }

    /**
     * 删除IP白名单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:ipWhiteList:remove")
    @Log(title = "IP白名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(ipWhiteListService.deleteWithValidByIds(List.of(ids), true));
    }
}
