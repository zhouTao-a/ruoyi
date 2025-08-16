package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecReportBo;
import org.dromara.mes.rec.domain.vo.RecReportVo;
import org.dromara.mes.rec.domain.RecReport;
import org.dromara.mes.rec.mapper.RecReportMapper;
import org.dromara.mes.rec.service.IRecReportService;

import java.util.Date;
import java.util.List;
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
        RecReportBo bo = new RecReportBo();
        bo.setId(id);
        return queryPageList(bo, new PageQuery(1, 1)).getRows().get(0);
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
        //只查询当前登录人的报告
        bo.setUserId(LoginHelper.getUserId());
        if (bo.getEndReportDate() != null) {
            bo.setEndReportDate(DateUtils.addDays(bo.getEndReportDate(), 1));
        }
        Page<RecReportVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
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
        return queryPageList(bo, new PageQuery()).getRows();
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
        assert add != null;
        validEntityBeforeSave(add);
        add.setUserId(LoginHelper.getUserId());
        return baseMapper.insert(add) > 0;
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
        assert update != null;
        validEntityBeforeSave(update);
        LambdaUpdateWrapper<RecReport> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RecReport::getId, update.getId())
            .set(RecReport::getReportDate, update.getReportDate())
            .set(RecReport::getReportType, update.getReportType())
            .set(RecReport::getContent, update.getContent())
            .set(RecReport::getSummary, update.getSummary())
            .set(RecReport::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecReport entity){
        boolean exists = baseMapper.exists(Wrappers.<RecReport>lambdaQuery()
            .eq(RecReport::getReportType, entity.getReportType())
            .eq(RecReport::getReportDate, entity.getReportDate())
            .ne(entity.getId() != null, RecReport::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("该报告已存在!");
        }
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
        return baseMapper.deleteByIds(ids) > 0;
    }
}
