package org.dromara.mes.rec.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mes.rec.domain.RecReport;
import org.dromara.mes.rec.domain.vo.RecReportVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 报告Mapper接口
 *
 * @author allen
 * @date 2025-08-09
 */
@Mapper
public interface RecReportMapper extends BaseMapperPlus<RecReport, RecReportVo> {

}
