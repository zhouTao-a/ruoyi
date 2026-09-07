package org.dromara.mes.rec.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.mes.rec.domain.bo.RecIntrospectBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectVo;

import java.util.List;

/**
 * 自省主题
 */
public interface IRecIntrospectService {

    RecIntrospectVo queryById(Long id);

    TableDataInfo<RecIntrospectVo> queryPageList(RecIntrospectBo bo, PageQuery pageQuery);

    List<RecIntrospectVo> queryList(RecIntrospectBo bo);

    Boolean insertByBo(RecIntrospectBo bo);

    Boolean updateByBo(RecIntrospectBo bo);

    Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid);

    /**
     * 修改生效状态。设为生效时，同一用户其它主题全部失效。
     */
    Boolean updateStatusById(Long id, String status);

    /**
     * 当前登录用户：生效主题 + 今日明细
     */
    RecIntrospectVo queryCurrent();

    /**
     * 日报用：今日生效主题 + 昨日明细
     */
    RecIntrospectDigestVo queryDigestByUserId(Long userId);
}
