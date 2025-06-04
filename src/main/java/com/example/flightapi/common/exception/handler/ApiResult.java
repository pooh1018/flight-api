package com.example.flightapi.common.exception.handler;

import lombok.Data;

import org.springframework.lang.Nullable;
import java.io.Serializable;

/**
 * 调用Api时的返回值封装类型。
 */
@Data
public class ApiResult<T> implements Serializable {

    /**
     * 处理是否正常完成
     */
    private boolean success;
    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误时间
     */
    private Long timestamp;
    /**
     * 错误内容
     */
    private String message;
    /**
     * Api返回的业务数据
     */
    private T data;

    private ApiResult() {
        timestamp = System.currentTimeMillis();
    }

    public static ApiResult<Void> fail(String code) {
        return fail(code, null);
    }

    public static ApiResult<Void> failMessage(String message) {
        return fail(null, message);
    }


    public static ApiResult<Void> fail(String code, String message) {
        return fail(code, message, null);
    }

    public static <T> ApiResult<T> fail(String code, T data) {
        return fail(code, null, data);
    }

    public static <T> ApiResult<T> fail(String code, String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.success = false;
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> ApiResult<T> success() {
        return success(null);
    }
}
