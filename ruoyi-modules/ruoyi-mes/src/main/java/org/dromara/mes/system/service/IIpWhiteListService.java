package org.dromara.mes.system.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.mes.system.domain.bo.IpWhiteListBo;
import org.dromara.mes.system.domain.vo.IpWhiteListVo;

import java.util.Collection;
import java.util.List;

/**
 * IP白名单Service接口
 *
 * @author Lion Li
 * @date 2025-05-22
 */
public interface IIpWhiteListService {

    /**
     * 查询IP白名单
     *
     * @param id 主键
     * @return IP白名单
     */
    IpWhiteListVo queryById(Long id);

    /**
     * 分页查询IP白名单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return IP白名单分页列表
     */
    TableDataInfo<IpWhiteListVo> queryPageList(IpWhiteListBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的IP白名单列表
     *
     * @param bo 查询条件
     * @return IP白名单列表
     */
    List<IpWhiteListVo> queryList(IpWhiteListBo bo);

    /**
     * 新增IP白名单
     *
     * @param bo IP白名单
     * @return 是否新增成功
     */
    Boolean insertByBo(IpWhiteListBo bo);

    /**
     * 修改IP白名单
     *
     * @param bo IP白名单
     * @return 是否修改成功
     */
    Boolean updateByBo(IpWhiteListBo bo);

    /**
     * 校验并批量删除IP白名单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
