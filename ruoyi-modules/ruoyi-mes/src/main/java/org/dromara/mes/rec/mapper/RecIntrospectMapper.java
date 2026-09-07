package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.mes.rec.domain.RecIntrospect;
import org.dromara.mes.rec.domain.bo.RecIntrospectBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectVo;

/**
 * 自省主题 Mapper
 */
@Mapper
public interface RecIntrospectMapper extends BaseMapperPlus<RecIntrospect, RecIntrospectVo> {

    Page<RecIntrospectVo> queryPageList(Page<Object> build, @Param("param") RecIntrospectBo bo);

    RecIntrospectVo selectActiveByUserId(@Param("userId") Long userId);
}
