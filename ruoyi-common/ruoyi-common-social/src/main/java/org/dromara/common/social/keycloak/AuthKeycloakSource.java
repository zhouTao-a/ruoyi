package org.dromara.common.social.keycloak;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Keycloak 数据源
 *
 * @author Lion Li
 */
public enum AuthKeycloakSource implements AuthSource {

    KEYCLOAK {

        @Override
        public String authorize() {
            return AuthKeycloakRequest.SERVER_URL + "/realms/" + AuthKeycloakRequest.REALM + "/protocol/openid-connect/auth";
        }

        @Override
        public String accessToken() {
            return AuthKeycloakRequest.SERVER_URL + "/realms/" + AuthKeycloakRequest.REALM + "/protocol/openid-connect/token";
        }

        @Override
        public String userInfo() {
            return AuthKeycloakRequest.SERVER_URL + "/realms/" + AuthKeycloakRequest.REALM + "/protocol/openid-connect/userinfo";
        }

        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthKeycloakRequest.class;
        }
    };

}
