package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.rec.domain.RecReport;
import org.dromara.mes.rec.domain.bo.RecReportBo;
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

    /**
     * 查询列表
     *
     * @param bo 查询参数
     * @return 查询结果
     */
    Page<RecReportVo> queryPageList(Page<Object> build,
                                    @Param("param") RecReportBo bo);
}
