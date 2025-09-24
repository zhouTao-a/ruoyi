package org.dromara.mes.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.mes.system.domain.IpWhiteList;
import org.dromara.mes.system.domain.vo.IpWhiteListVo;

/**
 * IP白名单Mapper接口
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@Mapper
public interface IpWhiteListMapper extends BaseMapperPlus<IpWhiteList, IpWhiteListVo> {

}
