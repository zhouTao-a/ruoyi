package org.dromara.mes.rec.service;

/**
 * 每日任务/目标摘要（定时发信与试发）
 */
public interface IRecDigestService {

    /**
     * 发送当天相关任务、目标、自省，以及该用户的一条感想。
     * 无任务参数时回退配置用户名。
     *
     * @return 邮件正文
     */
    String sendDailyDigest();

    /**
     * 按任务参数发送日报。参数示例：{"userName":"allen,hanhan"}
     *
     * @param jobParamsJson SnailJob 任务参数 JSON，可为 null
     * @return 邮件正文
     */
    String sendDailyDigest(String jobParamsJson);

    /**
     * 测试发信：与定时任务相同，按当天实际条件查询任务、目标、自省和感想
     *
     * @return 邮件正文
     */
    String testSendLatest();
}
