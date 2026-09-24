package org.dromara.mes.rec.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.mes.rec.domain.vo.RecSummaryVo;
import org.dromara.mes.rec.service.IRecSummaryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 记录汇总。登录即可查看自己的数量，不校验菜单权限。
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rec/summary")
public class RecSummaryController extends BaseController {

    private final IRecSummaryService recSummaryService;

    /**
     * 按月、季、年汇总当前用户新增的记录。
     *
     * @param periodType month / quarter / year
     * @param period     2026-09、2026-Q3、2026
     */
    @GetMapping
    public R<RecSummaryVo> summarize(@RequestParam String periodType, @RequestParam String period) {
        return R.ok(recSummaryService.summarize(periodType, period));
    }
}
