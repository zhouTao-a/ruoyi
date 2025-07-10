package org.dromara.job.msg;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.model.ExecuteResult;
import jakarta.annotation.Resource;
import org.dromara.mes.msg.service.IMsgDayMatterService;
import org.springframework.stereotype.Component;

/**
 * 定时更新下次执行时间
 */
@Component
@JobExecutor(name = "MsgDayMatterJobExecutor")
public class MsgDayMatterJobExecutor {

    @Resource
    private IMsgDayMatterService service;

    public ExecuteResult jobExecute() {
        service.updateNextNotifyTime();
        return ExecuteResult.success("更新成功");
    }
}
