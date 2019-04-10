package org.unamedgroup.conference.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWTToken
 *
 * @author EndangeredFish
 * @mender liumengxiao
 * @date 2019/04/10
 */
public class JWTToken implements AuthenticationToken {
    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}