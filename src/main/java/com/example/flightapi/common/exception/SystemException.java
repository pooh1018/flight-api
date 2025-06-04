package com.example.flightapi.common.exception;

import com.example.flightapi.common.constants.MessageCode;

public class SystemException extends RuntimeException {
    private final String code;

    public SystemException(String code) {
        this.code = code;
    }

    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }
    public SystemException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
    public SystemException(Throwable cause) {
        this(MessageCode.UNKNOWN, cause);
    }

    public String getCode() {
        return code;
    }
}
