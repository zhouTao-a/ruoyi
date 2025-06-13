package org.dromara.mes.msg.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.mes.utils.LunarSolarUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 日期访问类
 *
 * @author zhout
 * @date 2025-06-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/msg/date")
public class DateBaseController extends BaseController {

    /**
     * 农历转公历
     *
     * @param date 农历日期
     * @return 公历日期
     */
    @GetMapping("getSolarDate")
    public R<Date> getSolarDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        return R.ok(LunarSolarUtils.lunarToSolar(date).get(0));
    }

    /**
     * 公历转农历
     *
     * @param date 公历日期
     * @return 农历日期
     */
    @GetMapping("getLunarDate")
    public R<Date> getLunarDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        return R.ok(LunarSolarUtils.solarToLunar(date));
    }
}
