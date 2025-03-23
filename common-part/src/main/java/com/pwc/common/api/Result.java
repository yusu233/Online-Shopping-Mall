package com.pwc.common.api;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回结果封装类
 * @param <T>
 */
@Getter
@Setter
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    protected Result() {
    }

    protected Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<T>(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> Result<T> failed(ResultCodeEnum errorCode) {
        return new Result<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static <T> Result<T> failed(ResultCodeEnum errorCode,String message) {
        return new Result<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>(ResultCodeEnum.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCodeEnum.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCodeEnum.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>(ResultCodeEnum.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<T>(ResultCodeEnum.UNAUTHORIZED.getCode(), ResultCodeEnum.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<T>(ResultCodeEnum.FORBIDDEN.getCode(), ResultCodeEnum.FORBIDDEN.getMessage(), data);
    }
}
