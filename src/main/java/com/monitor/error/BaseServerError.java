package com.monitor.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: chenjh
 * @description:
 * @create: 2019-06-22 15:05
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseServerError extends RuntimeException {


    private Integer code;
    private String content;


    public BaseServerError(Integer code) {
        super("code: " + code);
        this.code = code;
    }

    public BaseServerError(Integer code, String content) {
        super("code: " + code);
        this.code = code;
        this.content = content;
    }

    public static final BaseServerError INTERNAL_ERROR = new BaseServerError(-1);
    public static final BaseServerError ACCESS_DENIED = new BaseServerError(1001);
}
