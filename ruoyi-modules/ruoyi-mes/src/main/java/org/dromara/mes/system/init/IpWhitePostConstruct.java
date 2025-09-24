package org.dromara.mes.system.init;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.dromara.mes.system.service.IIpWhiteListService;
import org.springframework.stereotype.Component;

@Component
public class IpWhitePostConstruct {

    @Resource
    private IIpWhiteListService ipWhiteListService;

    @PostConstruct
    public void init() {
        ipWhiteListService.refreshIpWhite();
    }
}
