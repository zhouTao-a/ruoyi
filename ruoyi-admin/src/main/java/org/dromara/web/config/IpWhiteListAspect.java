package org.dromara.web.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.mes.system.domain.IpWhiteList;
import org.dromara.mes.system.mapper.IpWhiteListMapper;
import org.dromara.mes.system.utils.IpUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Aspect
@Component
public class IpWhiteListAspect {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IpWhiteListMapper ipWhiteListMapper;

    private static final String REDIS_KEY = "ip:white:list";

    @Pointcut("@annotation(org.dromara.web.annotation.CheckIpWhiteList)")
    public void checkIpWhiteList() {}

    @Before("checkIpWhiteList()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        String ip = IpUtils.getIpAddr(request);

        // 判断是否是内网IP，如果是就直接放行
        if (isInternalIp(ip)) {
            return;
        }

        // 1. 先查 Redis
        Integer status = RedisUtils.getCacheObject(REDIS_KEY + ip);
        if (status != null && status == 1) return;

        // 2. 再查数据库
        IpWhiteList ipWhiteList = ipWhiteListMapper.selectOne(
                Wrappers.<IpWhiteList>lambdaQuery().eq(IpWhiteList::getIpAddress, ip));

        if (ipWhiteList != null && ipWhiteList.getStatus() == 1) {
            RedisUtils.setCacheObject(REDIS_KEY + ip, 1, Duration.ofMinutes(30));// 加入缓存,超时时间30分钟
            return;
        }

        // 3. 不在白名单，则插入待审核
        if (ipWhiteList == null) {
            ipWhiteList = new IpWhiteList();
            ipWhiteList.setIpAddress(ip);
            ipWhiteList.setStatus(0);
            ipWhiteList.setRemark("系统自动加入待授权");
            ipWhiteListMapper.insert(ipWhiteList);
        }
        throw new SecurityException("IP未授权,请联系后台管理员授权后再次登录！");
    }

    /**
     * 判断是否是内网访问
     * @param ip ip
     * @return 判断结果
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null) {
            return false;
        }
        return ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.16.")
            || ip.equals("127.0.0.1") || ip.equals("localhost");
    }

}
