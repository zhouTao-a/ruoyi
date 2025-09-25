package org.dromara.mes.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.mapper.RecReflectionMapper;
import org.dromara.mes.test.service.IPhantomReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhantomReadServiceImpl implements IPhantomReadService {

    private final RecReflectionMapper recReflectionMapper;

    @Override
    public List<RecReflection> selectList() {
        return recReflectionMapper.selectList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertIntoTestData(long l) {
        RecReflection r = new RecReflection();
        r.setUserId(1L);
        r.setTitle("测试数据");
        r.setContent("测试数据");
        r.setSynopsis("测试数据");
        r.setSourceType("测试数据");
        r.setSourceName("测试数据");
        r.setSourceLink("测试数据");
        r.setId(l);
        recReflectionMapper.insert(r);
    }
}
