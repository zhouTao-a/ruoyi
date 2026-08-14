package org.dromara.mes.rec.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.mes.rec.service.IRecDigestService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每日任务/目标摘要
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/digest")
public class RecDigestController extends BaseController {

    private final IRecDigestService recDigestService;

    /**
     * 测试：按当天实际条件查询任务、目标与感想，发信并返回正文
     */
    @SaIgnore
    @GetMapping("/testSendLatest")
    public R<String> testSendLatest() {
        return R.ok(recDigestService.testSendLatest());
    }
}
