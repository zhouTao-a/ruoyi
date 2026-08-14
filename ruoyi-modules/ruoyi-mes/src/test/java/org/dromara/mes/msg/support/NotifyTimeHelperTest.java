package org.dromara.mes.msg.support;

import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("dev")
@DisplayName("下次通知时间计算")
class NotifyTimeHelperTest {

    @Test
    @DisplayName("无时分秒的每年事件应落到未来某年当天 06:00")
    void yearlyDateOnly_usesSixAm() {
        Date dayTarget = DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, "2020-03-15 00:00:00");
        Date next = NotifyTimeHelper.calculate(dayTarget, "yearly", "solar");
        LocalDateTime ldt = LocalDateTime.ofInstant(next.toInstant(), ZoneId.systemDefault());
        assertEquals(15, ldt.getDayOfMonth());
        assertEquals(3, ldt.getMonthValue());
        assertEquals(NotifyTimeHelper.DATE_ONLY_HOUR, ldt.getHour());
        assertEquals(0, ldt.getMinute());
        assertTrue(ldt.isAfter(LocalDateTime.now()));
        assertTrue(ldt.toLocalDate().getYear() >= LocalDate.now().getYear());
    }

    @Test
    @DisplayName("带具体时刻的事件保持原时刻")
    void yearlyWithClock_keepsOriginalTime() {
        Date dayTarget = DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, "2020-03-15 14:30:00");
        Date next = NotifyTimeHelper.calculate(dayTarget, "yearly", "solar");
        LocalDateTime ldt = LocalDateTime.ofInstant(next.toInstant(), ZoneId.systemDefault());
        assertEquals(14, ldt.getHour());
        assertEquals(30, ldt.getMinute());
    }
}
