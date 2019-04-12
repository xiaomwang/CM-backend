package org.unamedgroup.conference.entity.temp;

import lombok.Data;

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
    }
}
