package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecReportVo;
import org.dromara.mes.rec.domain.bo.RecReportBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 报告Service接口
 *
 * @author allen
 * @date 2025-08-09
 */
public interface IRecReportService {

    /**
     * 查询报告
     *
     * @param id 主键
     * @return 报告
     */
    RecReportVo queryById(Long id);

    /**
     * 分页查询报告列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 报告分页列表
     */
    TableDataInfo<RecReportVo> queryPageList(RecReportBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的报告列表
     *
     * @param bo 查询条件
     * @return 报告列表
     */
    List<RecReportVo> queryList(RecReportBo bo);

    /**
     * 新增报告
     *
     * @param bo 报告
     * @return 是否新增成功
     */
    Boolean insertByBo(RecReportBo bo);

    /**
     * 修改报告
     *
     * @param bo 报告
     * @return 是否修改成功
     */
    Boolean updateByBo(RecReportBo bo);

    /**
     * 校验并批量删除报告信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
