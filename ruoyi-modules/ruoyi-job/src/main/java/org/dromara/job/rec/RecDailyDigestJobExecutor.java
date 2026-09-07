package org.dromara.job.rec;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.model.dto.ExecuteResult;
import jakarta.annotation.Resource;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.mes.rec.service.IRecDigestService;
import org.springframework.stereotype.Component;

/**
 * 每天按任务参数中的用户名发送当天任务、目标、自省及感想。
 * 参数示例：{"userName":"allen,hanhan"}，仅给存在且维护了邮箱的用户发信。
 * 早晚报共用同一执行器与同一套早报文案。
 */
@Component
@JobExecutor(name = "RecDailyDigestJobExecutor")
public class RecDailyDigestJobExecutor {

    @Resource
    private IRecDigestService recDigestService;

    public ExecuteResult jobExecute(JobArgs jobArgs) {
        String result = recDigestService.sendDailyDigest(toParamsJson(jobArgs));
        return ExecuteResult.success(result);
    }

    private String toParamsJson(JobArgs jobArgs) {
        if (jobArgs == null || jobArgs.getJobParams() == null) {
            return null;
        }
        Object params = jobArgs.getJobParams();
        if (params instanceof String text) {
            return text;
        }
        return JsonUtils.toJsonString(params);
    }
}
