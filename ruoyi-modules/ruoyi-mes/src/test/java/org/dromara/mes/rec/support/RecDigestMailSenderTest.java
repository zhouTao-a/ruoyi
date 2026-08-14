package org.dromara.mes.rec.support;

import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("dev")
@DisplayName("日报邮件正文")
class RecDigestMailSenderTest {

    private final RecDigestMailSender sender = new RecDigestMailSender();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sender, "recUrl", "http://www.allen-z.cn/rec-total");
    }

    @Test
    @DisplayName("正文顺序为任务、目标、感想、访问地址")
    void buildContent_order() {
        RecTaskVo task = new RecTaskVo();
        task.setTitle("写周报");
        task.setContent("把本周进展写完");
        task.setStatus("pending");

        RecGoalVo goal = new RecGoalVo();
        goal.setTitle("读完一本书");
        goal.setContent("每天读 20 页");
        goal.setStatus("in_progress");

        RecReflectionVo reflection = new RecReflectionVo();
        reflection.setTitle("今日感想");
        reflection.setContent("继续坚持");

        String body = sender.buildContent(List.of(task), List.of(goal), reflection);

        int taskPos = body.indexOf("【任务】");
        int goalPos = body.indexOf("【目标】");
        int reflectionPos = body.indexOf("【感想】");
        int urlPos = body.indexOf("访问地址：http://www.allen-z.cn/rec-total");
        assertTrue(taskPos >= 0 && taskPos < goalPos);
        assertTrue(goalPos < reflectionPos);
        assertTrue(reflectionPos < urlPos);
        assertTrue(body.contains("写周报"));
        assertTrue(body.contains("描述：把本周进展写完"));
        assertTrue(body.contains("读完一本书"));
        assertTrue(body.contains("内容：每天读 20 页"));
        assertTrue(body.contains("继续坚持"));
    }

    @Test
    @DisplayName("没有数据时仍保留三段和链接")
    void buildContent_empty_keepsSections() {
        String body = sender.buildContent(List.of(), List.of(), null);
        assertTrue(body.contains("暂无任务"));
        assertTrue(body.contains("暂无目标"));
        assertTrue(body.contains("暂无感想"));
        assertTrue(body.contains("访问地址：http://www.allen-z.cn/rec-total"));
    }
}
