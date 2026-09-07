package org.dromara.mes.rec.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.mes.rec.domain.bo.RecIntrospectItemBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectItemVo;

import java.util.List;

/**
 * 自省按天明细
 */
public interface IRecIntrospectItemService {

    RecIntrospectItemVo queryById(Long id);

    TableDataInfo<RecIntrospectItemVo> queryPageList(RecIntrospectItemBo bo, PageQuery pageQuery);

    Boolean insertByBo(RecIntrospectItemBo bo);

    Boolean updateByBo(RecIntrospectItemBo bo);

    Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid);
}
