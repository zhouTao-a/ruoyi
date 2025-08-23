package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.bo.RecReflectionBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 感想Service接口
 *
 * @author allen
 * @date 2025-08-09
 */
public interface IRecReflectionService {

    /**
     * 查询感想
     *
     * @param id 主键
     * @return 感想
     */
    RecReflectionVo queryById(Long id);

    /**
     * 分页查询感想列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 感想分页列表
     */
    TableDataInfo<RecReflectionVo> queryPageList(RecReflectionBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的感想列表
     *
     * @param bo 查询条件
     * @return 感想列表
     */
    List<RecReflectionVo> queryList(RecReflectionBo bo);

    /**
     * 新增感想
     *
     * @param bo 感想
     * @return 是否新增成功
     */
    Boolean insertByBo(RecReflectionBo bo);

    /**
     * 修改感想
     *
     * @param bo 感想
     * @return 是否修改成功
     */
    Boolean updateByBo(RecReflectionBo bo);

    /**
     * 校验并批量删除感想信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
