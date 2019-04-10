package org.unamedgroup.conference.entity.temp;

import lombok.Data;

/**
 * FailureInfo
 *
 * @author liumengxiao
 * @date 2019/04/10
 */

@Data
public class FailureInfo extends Info {
    private String message;

    public FailureInfo(String message) {
        super(-1);
        this.message = message;
    }
}
