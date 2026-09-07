package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.mes.rec.domain.RecIntrospect;
import org.dromara.mes.rec.domain.RecIntrospectItem;
import org.dromara.mes.rec.domain.bo.RecIntrospectBo;
import org.dromara.mes.rec.domain.bo.RecIntrospectItemBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectItemVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectVo;
import org.dromara.mes.rec.mapper.RecIntrospectItemMapper;
import org.dromara.mes.rec.mapper.RecIntrospectMapper;
import org.dromara.mes.rec.service.IRecIntrospectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 自省主题：维护标题与唯一生效。
 */
@RequiredArgsConstructor
@Service
public class RecIntrospectServiceImpl implements IRecIntrospectService {

    private final RecIntrospectMapper baseMapper;
    private final RecIntrospectItemMapper itemMapper;

    @Override
    public RecIntrospectVo queryById(Long id) {
        RecIntrospectBo bo = new RecIntrospectBo();
        bo.setId(id);
        List<RecIntrospectVo> rows = queryPageList(bo, new PageQuery(1, 1)).getRows();
        if (rows.isEmpty()) {
            throw new ServiceException("数据不存在");
        }
        return rows.get(0);
    }

    @Override
    public TableDataInfo<RecIntrospectVo> queryPageList(RecIntrospectBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        Page<RecIntrospectVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    @Override
    public List<RecIntrospectVo> queryList(RecIntrospectBo bo) {
        return queryPageList(bo, new PageQuery()).getRows();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(RecIntrospectBo bo) {
        RecIntrospect add = MapstructUtils.convert(bo, RecIntrospect.class);
        assert add != null;
        add.setUserId(LoginHelper.getUserId());
        if (StringUtils.isBlank(add.getStatus())) {
            add.setStatus(RecIntrospect.STATUS_INACTIVE);
        }
        if (add.getSortOrder() == null) {
            add.setSortOrder(0L);
        }
        validEntityBeforeSave(add);
        // 新增即生效时，先把其它主题失效，保证同时只有一条在线
        if (RecIntrospect.STATUS_ACTIVE.equals(add.getStatus())) {
            deactivateOthers(add.getUserId(), null);
        }
        return baseMapper.insert(add) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(RecIntrospectBo bo) {
        RecIntrospect update = MapstructUtils.convert(bo, RecIntrospect.class);
        assert update != null;
        RecIntrospect exist = requireOwn(update.getId());
        update.setUserId(exist.getUserId());
        validEntityBeforeSave(update);
        if (RecIntrospect.STATUS_ACTIVE.equals(update.getStatus())) {
            deactivateOthers(exist.getUserId(), exist.getId());
        }
        LambdaUpdateWrapper<RecIntrospect> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RecIntrospect::getId, update.getId())
            .eq(RecIntrospect::getUserId, exist.getUserId())
            .set(RecIntrospect::getTitle, update.getTitle())
            .set(RecIntrospect::getStatus, update.getStatus())
            .set(RecIntrospect::getSortOrder, update.getSortOrder())
            .set(RecIntrospect::getUpdateTime, new Date());
        return baseMapper.update(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        Long userId = LoginHelper.getUserId();
        itemMapper.delete(Wrappers.<RecIntrospectItem>lambdaQuery()
            .in(RecIntrospectItem::getIntrospectId, ids)
            .eq(RecIntrospectItem::getUserId, userId));
        return baseMapper.delete(Wrappers.<RecIntrospect>lambdaQuery()
            .in(RecIntrospect::getId, ids)
            .eq(RecIntrospect::getUserId, userId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatusById(Long id, String status) {
        RecIntrospect exist = requireOwn(id);
        if (RecIntrospect.STATUS_ACTIVE.equals(status)) {
            deactivateOthers(exist.getUserId(), exist.getId());
        }
        LambdaUpdateWrapper<RecIntrospect> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RecIntrospect::getId, id)
            .eq(RecIntrospect::getUserId, exist.getUserId())
            .set(RecIntrospect::getStatus, status)
            .set(RecIntrospect::getUpdateTime, new Date());
        return baseMapper.update(wrapper) > 0;
    }

    @Override
    public RecIntrospectVo queryCurrent() {
        Long userId = LoginHelper.getUserId();
        RecIntrospectVo active = baseMapper.selectActiveByUserId(userId);
        if (active == null) {
            return null;
        }
        RecIntrospectItemBo itemBo = new RecIntrospectItemBo();
        itemBo.setUserId(userId);
        itemBo.setIntrospectId(active.getId());
        itemBo.setOccurDate(todayStart());
        active.setItems(itemMapper.queryPageList(new PageQuery(100, 1).build(), itemBo).getRecords());
        return active;
    }

    @Override
    public RecIntrospectDigestVo queryDigestByUserId(Long userId) {
        return TenantHelper.ignore(() -> {
            RecIntrospectDigestVo digest = new RecIntrospectDigestVo();
            RecIntrospectVo active = baseMapper.selectActiveByUserId(userId);
            if (active != null) {
                digest.setTodayTitle(active.getTitle());
            }
            List<RecIntrospectItemVo> yesterdayItems = itemMapper.selectByUserIdAndDate(userId, yesterdayStart());
            if (yesterdayItems != null && !yesterdayItems.isEmpty()) {
                digest.setYesterdayTitle(yesterdayItems.get(0).getIntrospectTitle());
                digest.setYesterdayItems(yesterdayItems.stream().map(RecIntrospectItemVo::getContent).toList());
            }
            return digest;
        });
    }

    private RecIntrospect requireOwn(Long id) {
        RecIntrospect exist = baseMapper.selectById(id);
        if (exist == null || !LoginHelper.getUserId().equals(exist.getUserId())) {
            throw new ServiceException("数据不存在");
        }
        return exist;
    }

    /**
     * 将同一用户下除 keepId 外的主题全部失效。
     */
    private void deactivateOthers(Long userId, Long keepId) {
        LambdaUpdateWrapper<RecIntrospect> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RecIntrospect::getUserId, userId)
            .eq(RecIntrospect::getStatus, RecIntrospect.STATUS_ACTIVE)
            .ne(keepId != null, RecIntrospect::getId, keepId)
            .set(RecIntrospect::getStatus, RecIntrospect.STATUS_INACTIVE)
            .set(RecIntrospect::getUpdateTime, new Date());
        baseMapper.update(wrapper);
    }

    private void validEntityBeforeSave(RecIntrospect entity) {
        boolean exists = baseMapper.exists(Wrappers.<RecIntrospect>lambdaQuery()
            .eq(RecIntrospect::getTitle, entity.getTitle())
            .eq(RecIntrospect::getUserId, entity.getUserId())
            .ne(entity.getId() != null, RecIntrospect::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("自省主题已存在");
        }
    }

    private Date todayStart() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date yesterdayStart() {
        return Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
