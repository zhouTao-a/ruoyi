package org.dromara.job.msg;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.model.dto.ExecuteResult;
import jakarta.annotation.Resource;
import org.dromara.mes.msg.service.IMsgDayMatterService;
import org.springframework.stereotype.Component;

/**
 * 扫描到期事件：发送邮件后滚动下次通知时间。建议每分钟执行一次。
 */
@Component
@JobExecutor(name = "MsgDayMatterJobExecutor")
public class MsgDayMatterJobExecutor {

    @Resource
    private IMsgDayMatterService service;

    public ExecuteResult jobExecute() {
        service.updateNextNotifyTime();
        return ExecuteResult.success("处理成功");
    }
}
