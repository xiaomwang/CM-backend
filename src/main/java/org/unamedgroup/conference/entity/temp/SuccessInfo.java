package org.unamedgroup.conference.entity.temp;

import lombok.Data;

/**
 * SuccessInfo
 *
 * @author liumengxiao
 * @date 2019/04/10
 */

@Data
public class SuccessInfo {
    private Integer status;
    private Object data;

    public SuccessInfo(Object data) {
        status = 0;
        this.data = data;
    }
}
