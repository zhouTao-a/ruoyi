package org.dromara.mes.rec.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecReportBo;
import org.dromara.mes.rec.domain.vo.RecReportVo;
import org.dromara.mes.rec.domain.RecReport;
import org.dromara.mes.rec.mapper.RecReportMapper;
import org.dromara.mes.rec.service.IRecReportService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 报告Service业务层处理
 *
 * @author allen
 * @date 2025-08-09
 */
@RequiredArgsConstructor
@Service
public class RecReportServiceImpl implements IRecReportService {

    private final RecReportMapper baseMapper;

    /**
     * 查询报告
     *
     * @param id 主键
     * @return 报告
     */
    @Override
    public RecReportVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询报告列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 报告分页列表
     */
    @Override
    public TableDataInfo<RecReportVo> queryPageList(RecReportBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RecReport> lqw = buildQueryWrapper(bo);
        Page<RecReportVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的报告列表
     *
     * @param bo 查询条件
     * @return 报告列表
     */
    @Override
    public List<RecReportVo> queryList(RecReportBo bo) {
        LambdaQueryWrapper<RecReport> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RecReport> buildQueryWrapper(RecReportBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RecReport> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(RecReport::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getReportType()), RecReport::getReportType, bo.getReportType());
        lqw.eq(bo.getReportDate() != null, RecReport::getReportDate, bo.getReportDate());
        return lqw;
    }

    /**
     * 新增报告
     *
     * @param bo 报告
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecReportBo bo) {
        RecReport add = MapstructUtils.convert(bo, RecReport.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改报告
     *
     * @param bo 报告
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecReportBo bo) {
        RecReport update = MapstructUtils.convert(bo, RecReport.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecReport entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除报告信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
