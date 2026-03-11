package com.example.exceptions;

public class BaseException extends RuntimeException {
    private final int httpCode;
    private final int code;

    public BaseException(int httpCode, int code, String message) {
        super(message);
        this.httpCode = httpCode;
        this.code = code;
    }

    public BaseException(int httpCode, int code, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
        this.code = code;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.httpCode = 500;
        this.code = 5000;
    }

    public BaseException(String message) {
        super(message);
        this.httpCode = 500;  // значение по умолчанию
        this.code = 5000;      // значение по умолчанию
    }



    public int getHttpCode() {
        return httpCode;
    }

    public int getCode() {
        return code;
    }
}