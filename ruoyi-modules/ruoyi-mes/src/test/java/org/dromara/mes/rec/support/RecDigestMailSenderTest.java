package org.dromara.mes.rec.support;

import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        int introspectPos = body.indexOf("【自省】");
        int reflectionPos = body.indexOf("【感想】");
        int urlPos = body.indexOf("访问地址：http://www.allen-z.cn/rec-total");
        assertTrue(taskPos >= 0 && taskPos < goalPos);
        assertTrue(goalPos < introspectPos);
        assertTrue(introspectPos < reflectionPos);
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
        assertTrue(body.contains("今日暂无自省主题"));
        assertTrue(body.contains("暂无感想"));
        assertTrue(body.contains("访问地址：http://www.allen-z.cn/rec-total"));
    }

    @Test
    @DisplayName("自省在目标之后、感想之前，格式为今日主题加昨日回顾")
    void buildContent_introspect_morningFormat() {
        RecIntrospectDigestVo introspect = new RecIntrospectDigestVo();
        introspect.setTodayTitle("控制情绪");
        introspect.setYesterdayTitle("控制情绪");
        introspect.setYesterdayItems(List.of("开会时语气急了", "回家路上被插队", "和家人说话冲了"));

        String body = sender.buildContent(List.of(), List.of(), introspect, null);

        int goalPos = body.indexOf("【目标】");
        int introspectPos = body.indexOf("【自省】");
        int reflectionPos = body.indexOf("【感想】");
        assertTrue(goalPos >= 0 && goalPos < introspectPos);
        assertTrue(introspectPos < reflectionPos);
        assertTrue(body.contains("今日自省：控制情绪"));
        assertTrue(body.contains("昨日自省：控制情绪"));
        assertTrue(body.contains("昨日记录 3 次："));
        assertTrue(body.contains("1. 开会时语气急了"));
        assertTrue(body.contains("2. 回家路上被插队"));
        assertTrue(body.contains("3. 和家人说话冲了"));
    }

    @Test
    @DisplayName("无主题、无昨日明细时给出空态文案")
    void buildContent_introspect_empty() {
        String body = sender.buildContent(List.of(), List.of(), null, null);
        assertTrue(body.contains("【自省】"));
        assertTrue(body.contains("今日暂无自省主题"));
        assertTrue(body.contains("昨日暂无记录"));
        assertFalse(body.contains("昨日记录"));
    }
}
