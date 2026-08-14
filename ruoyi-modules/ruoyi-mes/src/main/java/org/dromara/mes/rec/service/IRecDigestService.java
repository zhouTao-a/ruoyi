package org.dromara.mes.rec.service;

/**
 * 每日任务/目标摘要（定时发信与试发）
 */
public interface IRecDigestService {

    /**
     * 发送当天相关任务、目标，以及 allen 的一条感想
     *
     * @return 邮件正文
     */
    String sendDailyDigest();

    /**
     * 测试发信：与每天 6 点相同，按当天实际条件查询任务、目标和感想
     *
     * @return 邮件正文
     */
    String testSendLatest();
}
