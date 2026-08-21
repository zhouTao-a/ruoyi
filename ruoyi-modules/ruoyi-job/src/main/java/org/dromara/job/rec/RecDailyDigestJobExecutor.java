package org.dromara.job.rec;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.model.dto.ExecuteResult;
import jakarta.annotation.Resource;
import org.dromara.mes.rec.service.IRecDigestService;
import org.springframework.stereotype.Component;

/**
 * 每天 06:00 发送当天相关任务、目标，以及 allen 的一条感想。
 */
@Component
@JobExecutor(name = "RecDailyDigestJobExecutor")
public class RecDailyDigestJobExecutor {

    @Resource
    private IRecDigestService recDigestService;

    public ExecuteResult jobExecute() {
        recDigestService.sendDailyDigest();
        return ExecuteResult.success("日报已发送");
    }
}
