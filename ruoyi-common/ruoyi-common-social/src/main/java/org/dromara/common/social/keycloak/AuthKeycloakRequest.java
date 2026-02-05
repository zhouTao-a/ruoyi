package org.dromara.common.social.keycloak;
import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.dromara.common.json.utils.JsonUtils;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.AuthChecker;
import me.zhyd.oauth.cache.AuthStateCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

/**
 * Keycloak 授权登录请求
 *
 * @author Lion Li
 */
public class AuthKeycloakRequest extends AuthDefaultRequest {

    public static String SERVER_URL;
    public static String REALM;

    public AuthKeycloakRequest(AuthConfig config) {
        super(config, AuthKeycloakSource.KEYCLOAK);
        // SERVER_URL and REALM are set by SocialUtils before creating this instance
    }

    public AuthKeycloakRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthKeycloakSource.KEYCLOAK, authStateCache);
        // SERVER_URL and REALM are set by SocialUtils before creating this instance
    }

    @Override
    public AuthToken getAccessToken(AuthCallback authCallback) {
        AuthChecker.checkCode(source, authCallback);
        String body = doPostAuthorizationCode(authCallback.getCode());
        Dict object = JsonUtils.parseMap(body);
        checkResponse(object);
        return AuthToken.builder()
            .accessToken(object.getStr("access_token"))
            .refreshToken(object.getStr("refresh_token"))
            .idToken(object.getStr("id_token"))
            .tokenType(object.getStr("token_type"))
            .scope(object.getStr("scope"))
            .build();
    }

    @Override
    public AuthUser getUserInfo(AuthToken authToken) {
        String body = doGetUserInfo(authToken);
        Dict object = JsonUtils.parseMap(body);
        checkResponse(object);
        return AuthUser.builder()
            .uuid(object.getStr("sub"))
            .username(object.getStr("preferred_username"))
            .nickname(object.getStr("nickname"))
            .avatar(object.getStr("picture"))
            .email(object.getStr("email"))
            .token(authToken)
            .source(source.toString())
            .build();
    }

    

    @Override
    protected String doPostAuthorizationCode(String code) {
        HttpRequest request = HttpRequest.post(SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .form("grant_type", "authorization_code")
            .form("client_id", config.getClientId())
            .form("client_secret",  config.getClientSecret())
            .form("code", code)
            .form("redirect_uri", config.getRedirectUri());

        HttpResponse response = request.execute();
        return response.body();
    }


    @Override
    protected String doGetUserInfo(AuthToken authToken) {
        HttpRequest request = HttpRequest.get(source.userInfo())
            .header("Authorization", "Bearer " + authToken.getAccessToken());
        HttpResponse response = request.execute();
        return response.body();
    }

    @Override
    public String authorize(String state) {
        // 构建授权URL，不添加PKCE参数
        return super.authorize(state);
    }

    private void checkResponse(Dict object) {
        // oauth/token 验证异常
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        // user 验证异常
        if (object.containsKey("message")) {
            throw new AuthException(object.getStr("message"));
        }
    }

}
