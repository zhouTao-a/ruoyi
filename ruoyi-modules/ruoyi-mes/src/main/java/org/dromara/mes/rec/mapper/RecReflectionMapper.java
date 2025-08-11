package org.dromara.mes.rec.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 感想Mapper接口
 *
 * @author allen
 * @date 2025-08-09
 */
@Mapper
public interface RecReflectionMapper extends BaseMapperPlus<RecReflection, RecReflectionVo> {

}
