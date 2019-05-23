package org.unamedgroup.conference.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.unamedgroup.conference.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JWTUtil
 *
 * @author EndangeredFish
 * @mender liumengxiao
 * @date 2019/04/10
 */
public class JWTUtil {

    // 过期时间 2h
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码(变体)
     * @return 是否正确
     */
    public static boolean verify(String token, String phoneNumber, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("phone_number", phoneNumber)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            System.err.println("无法通过token校验，请检查！");
            System.err.println(exception.toString());
        }
        return false;

    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的电话号码
     */
    public static String getPhoneNumber(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("phone_number").asString();
        } catch (JWTDecodeException e) {
            System.err.println("无法从token中获取电话号码！错误信息：");
            System.err.println(e.toString());
        }
        return null;
    }

    /**
     * 生成Token(签名),EXPIRE_TIME后过期
     *
     * @param phoneNumber 用户的手机号
     * @param secret      用户的密码(变体)
     * @return 加密的token
     */
    public static String generateToken(String phoneNumber, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("phone_number", phoneNumber)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            System.err.println("生成token出错，详细信息：");
            e.toString();
        }
        return null;
    }
}
