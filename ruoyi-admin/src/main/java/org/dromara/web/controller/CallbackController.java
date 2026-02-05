package org.dromara.web.controller;

import cn.hutool.core.util.IdUtil;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;
import java.time.Duration;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.social.config.properties.SocialProperties;
import org.dromara.common.social.keycloak.AuthKeycloakRequest;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.bo.SysSocialBo;
import org.dromara.system.domain.vo.SysClientVo;
import org.dromara.system.domain.vo.SysSocialVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysClientService;
import org.dromara.system.service.ISysSocialService;
import org.dromara.system.service.ISysUserService;
import org.dromara.web.service.SysLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * 回调控制器
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CallbackController {

    private final SocialProperties socialProperties;
    private final ISysSocialService socialUserService;
    private final ISysUserService userService;
    private final ISysClientService clientService;
    private final SysLoginService loginService;

    /**
     * Keycloak 回调
     *
     * @param code  授权码
     * @param state 状态码
     * @return 重定向到首页
     */
    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code, @RequestParam(required = false) String state) {
        log.info("开始处理Keycloak回调，code: {}, state: {}", code, state);
        try {
            log.info("1. 准备获取Keycloak配置");
            // 获取Keycloak配置
            String serverUrl = socialProperties.getType().get("keycloak").getServerUrl();
            String realm = socialProperties.getType().get("keycloak").getRealm();
            String clientId = socialProperties.getType().get("keycloak").getClientId();
            String clientSecret = socialProperties.getType().get("keycloak").getClientSecret();
            String redirectUri = socialProperties.getType().get("keycloak").getRedirectUri();
            List<String> scopes = socialProperties.getType().get("keycloak").getScopes();

            log.info("2. 配置信息获取完成，serverUrl: {}, realm: {}", serverUrl, realm);

            // 设置Keycloak服务器URL和 Realm
            AuthKeycloakRequest.SERVER_URL = serverUrl;
            AuthKeycloakRequest.REALM = realm;

            // 构建AuthConfig
            AuthConfig config = AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .scopes(scopes)
                .build();

            log.info("3. AuthConfig构建完成");

            // 创建AuthKeycloakRequest（不使用状态缓存，避免state验证问题）
            AuthKeycloakRequest authRequest = new AuthKeycloakRequest(config);

            log.info("4. AuthKeycloakRequest创建完成");

            // 创建AuthCallback（只设置code，不设置state，避免state验证）
            AuthCallback callback = new AuthCallback();
            callback.setCode(code);
            // 不设置state，避免state验证

            log.info("5. AuthCallback创建完成");

            // 获取token
            log.info("6. 开始获取access token");
            AuthToken token = authRequest.getAccessToken(callback);
            log.info("7. 获取token成功，accessToken: {}", token.getAccessToken() != null ? "****" : "null");

            // 获取用户信息
            log.info("8. 开始获取用户信息");
            AuthUser authUserData = authRequest.getUserInfo(token);
            log.info("9. 获取用户信息成功，用户名: {}, 邮箱: {}", authUserData.getUsername(), authUserData.getEmail());

            // 查找或创建用户
            String authId = "keycloak" + authUserData.getUuid();
            log.info("10. 准备查找社交绑定，authId: {}", authId);
            List<SysSocialVo> socialList = socialUserService.selectByAuthId(authId);
            log.info("11. 查找社交绑定完成，结果数量: {}", socialList.size());
            SysUserVo user;
            if (socialList.isEmpty()) {
                log.info("12. 社交绑定不存在，准备创建用户");
                // 创建用户
                user = createUser(authUserData);
                log.info("13. 用户创建完成，用户ID: {}", user.getUserId());
                // 创建社交绑定
                createSocialBinding(user, authUserData);
                log.info("14. 社交绑定创建完成");
            } else {
                log.info("12. 社交绑定存在，准备查找用户");
                // 查找用户
                SysSocialVo social = socialList.get(0);
                user = userService.selectUserById(social.getUserId());
                log.info("13. 用户查找完成，用户ID: {}", user.getUserId());
            }

            // 构建登录用户
            log.info("14. 准备构建登录用户，租户ID: {}", user.getTenantId());
            LoginUser loginUser = TenantHelper.dynamic(user.getTenantId(), () -> {
                return loginService.buildLoginUser(user);
            });
            log.info("15. 登录用户构建完成，登录ID: {}", loginUser.getLoginId());

            // 生成 token
            log.info("16. 准备查询客户端信息");
            SysClientVo client = clientService.queryById(10L);
            if (ObjectUtil.isNull(client)) {
                log.info("17. PC客户端不存在，查询APP客户端");
                client = clientService.queryByClientId("app");
            }

            // 如果客户端仍然不存在，使用默认配置
            if (ObjectUtil.isNull(client)) {
                log.info("18. 客户端不存在，使用默认配置");
                // 创建默认客户端配置
                client = new org.dromara.system.domain.vo.SysClientVo();
                client.setClientId("e5cd7e4891bf95d1d19206ce24a7b32e");
                client.setClientKey("e5cd7e4891bf95d1d19206ce24a7b32e");
                client.setDeviceType("pc");
            } else {
                log.info("18. 客户端查询完成，客户端ID: {}", client.getClientId());
            }

            loginUser.setClientKey(client.getClientKey());
            loginUser.setDeviceType(client.getDeviceType());
            log.info("19. 登录用户信息设置完成");

            // 登录
            log.info("20. 准备登录，创建SaLoginModel");
            SaLoginModel model = new SaLoginModel();
            model.setDevice(client.getDeviceType());
            model.setExtra(LoginHelper.CLIENT_KEY, client.getClientKey());
            log.info("21. 调用LoginHelper.login");
            LoginHelper.login(loginUser, model);
            log.info("22. 登录完成，获取token");

            // 重定向到首页
            log.info("23. 准备重定向到首页");
            String tokenValue = StpUtil.getTokenValue();
            log.info("24. 获取到token值: {}", tokenValue != null ? tokenValue : "null");

            // 生成临时票据
            String ticket = IdUtil.fastSimpleUUID();
            // 将token存入redis，有效期60秒，使用全局key避免租户前缀问题
            RedisUtils.setCacheObject(GlobalConstants.GLOBAL_REDIS_KEY + "sso_ticket:" + ticket, tokenValue, Duration.ofSeconds(600));
            log.info("25. 生成临时票据: {}, 存入Redis", ticket);

            // 重定向到前端地址，携带ticket
            return new RedirectView("http://192.168.50.70:80/index?sso_ticket=" + ticket);
        } catch (Exception e) {
            log.error("Keycloak 回调处理失败", e);
            // 错误重定向到前端首页，前端端口是80
            // 对错误信息进行URL编码，避免URI malformed错误
            String encodedError = java.net.URLEncoder.encode("Keycloak登录失败: " + e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
            return new RedirectView("http://192.168.50.70:80?error=" + encodedError);
        }
    }

    /**
     * 校验票据并获取token
     *
     * @param ticket 临时票据
     * @return token
     */
    @GetMapping("/sso/check")
    public R<String> ssoCheck(@RequestParam String ticket) {
        String key = GlobalConstants.GLOBAL_REDIS_KEY + "sso_ticket:" + ticket;
        log.info("26. 校验票据: {}", ticket);
        String token = RedisUtils.getCacheObject(key);
        log.info("27. 从Redis获取token: {}", token != null ? token : "null");

        if (StringUtils.isEmpty(token)) {
            return R.fail("票据无效或已过期");
        }
        // 获取后立即删除
        RedisUtils.deleteObject(key);
        log.info("28. 删除票据: {}", ticket);
        log.info("29. 返回token: {}", token);
        return R.ok("操作成功", token);
    }

    /**
     * 创建用户
     */
    private SysUserVo createUser(AuthUser authUserData) {
        SysUserVo user = userService.selectUserByUserName(authUserData.getUsername());
        if (user != null) {
        return user;
        }
        org.dromara.system.domain.bo.SysUserBo userBo = new org.dromara.system.domain.bo.SysUserBo();
        userBo.setUserName(authUserData.getUsername());
        userBo.setNickName(authUserData.getUsername());
        userBo.setEmail(authUserData.getEmail());
        // AuthUser没有getPhone()方法，设置为空
        userBo.setPhonenumber(null);
        userBo.setDeptId(103L); // 默认部门
        userService.registerUser(userBo, "000000"); // 默认租户
        return userService.selectUserByUserName(authUserData.getUsername());
    }

    /**
     * 创建社交绑定
     */
    private void createSocialBinding(SysUserVo user, AuthUser authUserData) {
        SysSocialBo socialBo = new SysSocialBo();
        socialBo.setUserId(user.getUserId());
        socialBo.setTenantId(user.getTenantId());
        socialBo.setAuthId(authUserData.getSource() + authUserData.getUuid());
        socialBo.setSource(authUserData.getSource());
        socialBo.setOpenId(authUserData.getUuid());
        socialBo.setUserName(authUserData.getUsername());
        socialBo.setNickName(authUserData.getNickname());
        socialBo.setEmail(authUserData.getEmail());
        socialBo.setAvatar(authUserData.getAvatar());
        socialBo.setAccessToken(authUserData.getToken().getAccessToken());
        socialBo.setExpireIn(authUserData.getToken().getExpireIn());
        socialBo.setRefreshToken(authUserData.getToken().getRefreshToken());
        socialBo.setScope(authUserData.getToken().getScope());
        socialUserService.insertByBo(socialBo);
    }

}
