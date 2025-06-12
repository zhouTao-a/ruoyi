package org.dromara.mes.system.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.dromara.common.core.utils.StringUtils;

public class IpUtils {
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) return "unknown";

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多级代理，取第一个非 unknown 的真实 IP
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
