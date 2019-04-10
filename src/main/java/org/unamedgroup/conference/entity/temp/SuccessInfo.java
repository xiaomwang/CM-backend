package org.unamedgroup.conference.entity.temp;

import lombok.Data;

/**
 * SuccessInfo
 *
 * @author liumengxiao
 * @date 2019/04/10
 */

@Data
public class SuccessInfo extends Info {
    private String data;

    public SuccessInfo(String data) {
        super(0);
        this.data = data;
    }
}
