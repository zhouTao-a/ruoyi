package org.dromara.mes.rec.domain.vo;

import lombok.Data;

/**
 * 记录汇总：当前用户在指定时段内新增的数量。
 */
@Data
public class RecSummaryVo {

    /** 新增任务 */
    private long taskCount;

    /** 新增报告 */
    private long reportCount;

    /** 新增感想 */
    private long reflectionCount;

    /** 新增目标 */
    private long goalCount;

    /** 新增自省主题 */
    private long introspectCount;

    /** 自省记录（按发生日期） */
    private long introspectItemCount;
}
