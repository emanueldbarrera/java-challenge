package jp.co.axa.apidemo.common;

import lombok.Getter;

@Getter
public class ApiBusinessException extends Exception {
    private final String placeCode;
    private final ErrorCode errorCode;

    public ApiBusinessException(String placeCode, ErrorCode errorCode, String message) {
        super(message);
        this.placeCode = placeCode;
        this.errorCode = errorCode;
    }
}
