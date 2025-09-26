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
    public void insertIntoTestDataNoTransaction(long l) {
        RecReflection r = new RecReflection();
        String string = "测试数据";
        r.setUserId(1L);
        r.setTitle(string);
        r.setContent(string);
        r.setSynopsis(string);
        r.setSourceType(string);
        r.setSourceName(string);
        r.setSourceLink(string);
        r.setId(l);
        recReflectionMapper.insert(r);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertIntoTestData(long l) {
        RecReflection r = new RecReflection();
        String string = "测试数据";
        r.setUserId(1L);
        r.setTitle(string);
        r.setContent(string);
        r.setSynopsis(string);
        r.setSourceType(string);
        r.setSourceName(string);
        r.setSourceLink(string);
        r.setId(l);
        recReflectionMapper.insert(r);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTestData(long id) {
        RecReflection r = new RecReflection();
        String string = "更新数据" + id;
        r.setUserId(1L);
        r.setTitle(string);
        r.setContent(string);
        r.setSynopsis(string);
        r.setSourceType(string);
        r.setSourceName(string);
        r.setSourceLink(string);
        r.setId(id);
        recReflectionMapper.updateById( r);
    }
    @Override
    public void updateTestDataNoTransaction(long id) {
        RecReflection r = new RecReflection();
        String string = "更新数据" + id;
        r.setUserId(1L);
        r.setTitle(string);
        r.setContent(string);
        r.setSynopsis(string);
        r.setSourceType(string);
        r.setSourceName(string);
        r.setSourceLink(string);
        r.setId(id);
        recReflectionMapper.updateById( r);
    }

    @Override
    public void updateTestDataRange(long start, long end) {
        recReflectionMapper.updateTestDataRange(start, end);
    }

    @Override
    public void deleteTestDataRange(long start, long end) {
        recReflectionMapper.deleteTestDataRange(start, end);
    }
}
