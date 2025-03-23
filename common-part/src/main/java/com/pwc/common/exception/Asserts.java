package com.pwc.common.exception;

import com.pwc.common.api.ResultCodeEnum;

/**
 * 断言处理类，用于抛出各种API异常
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(ResultCodeEnum errorCode) {
        throw new ApiException(errorCode);
    }
}
