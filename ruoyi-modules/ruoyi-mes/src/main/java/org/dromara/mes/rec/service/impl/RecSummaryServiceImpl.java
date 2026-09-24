package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.mes.rec.domain.RecIntrospect;
import org.dromara.mes.rec.domain.RecIntrospectItem;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.domain.RecReport;
import org.dromara.mes.rec.domain.RecTask;
import org.dromara.mes.rec.domain.vo.RecSummaryVo;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.mapper.RecIntrospectItemMapper;
import org.dromara.mes.rec.mapper.RecIntrospectMapper;
import org.dromara.mes.rec.mapper.RecReflectionMapper;
import org.dromara.mes.rec.mapper.RecReportMapper;
import org.dromara.mes.rec.mapper.RecTaskMapper;
import org.dromara.mes.rec.service.IRecSummaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 按创建时间统计当前用户在时段内的新增数量。自省记录按发生日期。
 */
@RequiredArgsConstructor
@Service
public class RecSummaryServiceImpl implements IRecSummaryService {

    private final RecTaskMapper taskMapper;
    private final RecReportMapper reportMapper;
    private final RecReflectionMapper reflectionMapper;
    private final RecGoalMapper goalMapper;
    private final RecIntrospectMapper introspectMapper;
    private final RecIntrospectItemMapper introspectItemMapper;

    @Override
    public RecSummaryVo summarize(String periodType, String period) {
        Range range = parseRange(periodType, period);
        Long userId = LoginHelper.getUserId();
        Date begin = toDate(range.begin());
        Date end = toDate(range.end());

        RecSummaryVo vo = new RecSummaryVo();
        vo.setTaskCount(taskMapper.selectCount(Wrappers.<RecTask>lambdaQuery()
            .eq(RecTask::getUserId, userId)
            .ge(RecTask::getCreateTime, begin)
            .lt(RecTask::getCreateTime, end)));
        vo.setReportCount(reportMapper.selectCount(Wrappers.<RecReport>lambdaQuery()
            .eq(RecReport::getUserId, userId)
            .ge(RecReport::getCreateTime, begin)
            .lt(RecReport::getCreateTime, end)));
        vo.setReflectionCount(reflectionMapper.selectCount(Wrappers.<RecReflection>lambdaQuery()
            .eq(RecReflection::getUserId, userId)
            .ge(RecReflection::getCreateTime, begin)
            .lt(RecReflection::getCreateTime, end)));
        vo.setGoalCount(goalMapper.selectCount(Wrappers.<RecGoal>lambdaQuery()
            .eq(RecGoal::getUserId, userId)
            .ge(RecGoal::getCreateTime, begin)
            .lt(RecGoal::getCreateTime, end)));
        vo.setIntrospectCount(introspectMapper.selectCount(Wrappers.<RecIntrospect>lambdaQuery()
            .eq(RecIntrospect::getUserId, userId)
            .ge(RecIntrospect::getCreateTime, begin)
            .lt(RecIntrospect::getCreateTime, end)));
        vo.setIntrospectItemCount(introspectItemMapper.selectCount(Wrappers.<RecIntrospectItem>lambdaQuery()
            .eq(RecIntrospectItem::getUserId, userId)
            .ge(RecIntrospectItem::getOccurDate, begin)
            .lt(RecIntrospectItem::getOccurDate, end)));
        return vo;
    }

    private Range parseRange(String periodType, String period) {
        if (periodType == null || period == null || period.isBlank()) {
            throw new ServiceException("汇总时段不能为空");
        }
        try {
            return switch (periodType) {
                case "month" -> monthRange(period);
                case "quarter" -> quarterRange(period);
                case "year" -> yearRange(period);
                default -> throw new ServiceException("不支持的汇总类型");
            };
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("汇总时段格式不正确");
        }
    }

    private Range monthRange(String period) {
        String[] parts = period.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        LocalDate begin = LocalDate.of(year, month, 1);
        return new Range(begin, begin.plusMonths(1));
    }

    private Range quarterRange(String period) {
        String[] parts = period.toUpperCase().split("-Q");
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        if (quarter < 1 || quarter > 4) {
            throw new ServiceException("季度必须是 1 到 4");
        }
        LocalDate begin = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
        return new Range(begin, begin.plusMonths(3));
    }

    private Range yearRange(String period) {
        int year = Integer.parseInt(period.trim());
        LocalDate begin = LocalDate.of(year, 1, 1);
        return new Range(begin, begin.plusYears(1));
    }

    private Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private record Range(LocalDate begin, LocalDate end) {
    }
}
