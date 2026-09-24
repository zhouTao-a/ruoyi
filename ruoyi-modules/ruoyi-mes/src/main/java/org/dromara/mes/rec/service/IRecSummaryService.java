package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecSummaryVo;

/**
 * 记录汇总
 */
public interface IRecSummaryService {

    /**
     * 按月、季、年统计当前用户的新增记录。
     *
     * @param periodType month / quarter / year
     * @param period     2026-09、2026-Q3、2026
     */
    RecSummaryVo summarize(String periodType, String period);
}
