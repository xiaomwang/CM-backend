package org.unamedgroup.conference.entity.temp;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unamedgroup.conference.security.UnauthorizedException;

/**
 * FailureInfo
 *
 * @author liumengxiao
 * @date 2019/04/10
 */

@Data
public class FailureInfo {
    private Integer status;
    private String message;

    public FailureInfo() {
        status = -1;
        message = "登录验证失败，请检查是否登录！";
    }

    public FailureInfo(Integer status, String message) {
        this.status = status;
        this.message = message;
        try {
            if (status == -1) {
                throw new UnauthorizedException(message);
            }
        } catch (Exception e) {
            System.err.println("用户鉴权失败！");
            System.err.println(message);

        }
    }
}
