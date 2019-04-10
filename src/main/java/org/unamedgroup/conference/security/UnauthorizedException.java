package org.unamedgroup.conference.security;

/**
 * UnauthorizedException
 * 手写几个未认证导致的异常
 *
 * @author EndangeredFish
 * @mender liumengxiao
 * @date 2019/04/10
 */

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
