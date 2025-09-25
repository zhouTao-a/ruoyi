package org.dromara.mes.test.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.mes.test.service.IPhantomReadService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 幻读测试
 *
 * @author allen
 */
@RestController
@RequestMapping("/phantomRead")
@RequiredArgsConstructor
@Transactional
public class PhantomReadController {

    private final IRecReflectionService iRecReflectionService;
    private final IPhantomReadService iPhantomReadService;

    /**
     * 幻读测试 select ... for update
     */
    @GetMapping("/selectForUpdate")
    public String selectForUpdate() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        long id = 100L;
        iPhantomReadService.insertIntoTestData(id);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        List<RecReflection> selectForUpdates = iRecReflectionService.selectForUpdate();
        System.out.println("当前读 select ... for update 数据记录数：" + selectForUpdates.size());
        iRecReflectionService.deleteById(id);
        return "当前读 select ... for update 结束";
    }

    /**
     * 当前读 select ... for share
     */
    @GetMapping("/selectForShare")
    public String selectForShare() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        long id = 100L;
        iPhantomReadService.insertIntoTestData(id);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        List<RecReflection> selectForUpdates = iRecReflectionService.selectForShare();
        System.out.println("当前读 select ... for share 数据记录数：" + selectForUpdates.size());
        iRecReflectionService.deleteById(id);
        //再次插入数据时，因为insertIntoTestData是新事务，而这个事物使用了共享读，不能插入数据，导致死锁
//        iPhantomReadService.insertIntoTestData();
        return "当前读 select ... for share 结束";
    }



}
