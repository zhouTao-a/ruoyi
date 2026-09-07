package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.mes.rec.domain.RecIntrospectItem;
import org.dromara.mes.rec.domain.bo.RecIntrospectItemBo;
import org.dromara.mes.rec.domain.vo.RecIntrospectItemVo;

import java.util.Date;
import java.util.List;

/**
 * 自省明细 Mapper
 */
@Mapper
public interface RecIntrospectItemMapper extends BaseMapperPlus<RecIntrospectItem, RecIntrospectItemVo> {

    Page<RecIntrospectItemVo> queryPageList(Page<Object> build, @Param("param") RecIntrospectItemBo bo);

    /**
     * 某用户某一天的全部明细（含所属主题标题）
     */
    List<RecIntrospectItemVo> selectByUserIdAndDate(@Param("userId") Long userId, @Param("occurDate") Date occurDate);
}
