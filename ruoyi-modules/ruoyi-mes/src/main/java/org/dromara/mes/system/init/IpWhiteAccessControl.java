package org.dromara.mes.system.init;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class IpWhiteAccessControl {

    /**
     * 内网IP白名单
     */
    private final CopyOnWriteArrayList<String> ipWhiteList = new CopyOnWriteArrayList<>();

    public boolean isIpWhite(String ip) {
        return ipWhiteList.contains(ip);
    }

    public void addIpWhite(String ip) {
        ipWhiteList.add(ip);
    }

    public void removeIpWhite(String ip) {
        ipWhiteList.remove(ip);
    }

    public void refreshIpWhite(List<String> newIpWhiteList) {
        CopyOnWriteArrayList<String> newList = new CopyOnWriteArrayList<>(newIpWhiteList);
        ipWhiteList.clear();
        ipWhiteList.addAll(newList);
    }
}
