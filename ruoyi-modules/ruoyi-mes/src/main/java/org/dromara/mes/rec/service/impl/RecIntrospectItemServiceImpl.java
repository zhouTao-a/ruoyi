package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.mes.rec.domain.RecIntrospect;
import org.dromara.mes.rec.domain.RecIntrospectItem;
import org.dromara.mes.rec.domain.bo.RecIntrospectItemBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectItemVo;
import org.dromara.mes.rec.mapper.RecIntrospectItemMapper;
import org.dromara.mes.rec.mapper.RecIntrospectMapper;
import org.dromara.mes.rec.service.IRecIntrospectItemService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 自省按天明细
 */
@RequiredArgsConstructor
@Service
public class RecIntrospectItemServiceImpl implements IRecIntrospectItemService {

    private final RecIntrospectItemMapper baseMapper;
    private final RecIntrospectMapper introspectMapper;

    @Override
    public RecIntrospectItemVo queryById(Long id) {
        RecIntrospectItemBo bo = new RecIntrospectItemBo();
        bo.setId(id);
        List<RecIntrospectItemVo> rows = queryPageList(bo, new PageQuery(1, 1)).getRows();
        if (rows.isEmpty()) {
            throw new ServiceException("数据不存在");
        }
        return rows.get(0);
    }

    @Override
    public TableDataInfo<RecIntrospectItemVo> queryPageList(RecIntrospectItemBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        Page<RecIntrospectItemVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    @Override
    public Boolean insertByBo(RecIntrospectItemBo bo) {
        RecIntrospectItem add = MapstructUtils.convert(bo, RecIntrospectItem.class);
        assert add != null;
        Long userId = LoginHelper.getUserId();
        requireOwnTheme(add.getIntrospectId(), userId);
        add.setUserId(userId);
        if (add.getSortOrder() == null) {
            add.setSortOrder(nextSort(add.getIntrospectId(), add.getOccurDate(), userId));
        }
        return baseMapper.insert(add) > 0;
    }

    @Override
    public Boolean updateByBo(RecIntrospectItemBo bo) {
        RecIntrospectItem update = MapstructUtils.convert(bo, RecIntrospectItem.class);
        assert update != null;
        RecIntrospectItem exist = requireOwnItem(update.getId());
        requireOwnTheme(update.getIntrospectId(), exist.getUserId());
        LambdaUpdateWrapper<RecIntrospectItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RecIntrospectItem::getId, exist.getId())
            .eq(RecIntrospectItem::getUserId, exist.getUserId())
            .set(RecIntrospectItem::getContent, update.getContent())
            .set(RecIntrospectItem::getOccurDate, update.getOccurDate())
            .set(RecIntrospectItem::getSortOrder, update.getSortOrder())
            .set(RecIntrospectItem::getUpdateTime, new Date());
        return baseMapper.update(wrapper) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        return baseMapper.delete(Wrappers.<RecIntrospectItem>lambdaQuery()
            .in(RecIntrospectItem::getId, ids)
            .eq(RecIntrospectItem::getUserId, LoginHelper.getUserId())) > 0;
    }

    private RecIntrospectItem requireOwnItem(Long id) {
        RecIntrospectItem exist = baseMapper.selectById(id);
        if (exist == null || !LoginHelper.getUserId().equals(exist.getUserId())) {
            throw new ServiceException("数据不存在");
        }
        return exist;
    }

    private void requireOwnTheme(Long introspectId, Long userId) {
        RecIntrospect theme = introspectMapper.selectById(introspectId);
        if (theme == null || !userId.equals(theme.getUserId())) {
            throw new ServiceException("自省主题不存在");
        }
    }

    private Long nextSort(Long introspectId, Date occurDate, Long userId) {
        RecIntrospectItemBo bo = new RecIntrospectItemBo();
        bo.setUserId(userId);
        bo.setIntrospectId(introspectId);
        bo.setOccurDate(occurDate);
        List<RecIntrospectItemVo> rows = baseMapper.queryPageList(new PageQuery(1000, 1).build(), bo).getRecords();
        long max = rows.stream().map(RecIntrospectItemVo::getSortOrder).filter(v -> v != null).mapToLong(Long::longValue).max().orElse(0L);
        return max + 1;
    }
}
