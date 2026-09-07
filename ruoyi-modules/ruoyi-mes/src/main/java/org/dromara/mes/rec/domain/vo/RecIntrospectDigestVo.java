package org.dromara.mes.rec.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 日报邮件中的自省摘要：今日生效主题 + 昨日明细。
 */
@Data
public class RecIntrospectDigestVo {

    /**
     * 当前生效的自省主题标题
     */
    private String todayTitle;

    /**
     * 昨日明细所属主题标题
     */
    private String yesterdayTitle;

    /**
     * 昨日情况说明，按记录顺序
     */
    private List<String> yesterdayItems = new ArrayList<>();
}
