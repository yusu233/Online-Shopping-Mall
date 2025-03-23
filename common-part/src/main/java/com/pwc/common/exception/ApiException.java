package com.pwc.common.exception;

import com.pwc.common.api.ResultCodeEnum;

/**
 * 自定义API异常
 */
public class ApiException extends RuntimeException {
    private ResultCodeEnum errorCode;

    public ApiException(ResultCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultCodeEnum getErrorCode() {
        return errorCode;
    }
}
