package jp.co.axa.apidemo.common;

import lombok.Getter;

/**
 * Custom Exception class for handling internal expected excetions
 *
 */
@Getter
public class ApiBusinessException extends Exception {
    private final String placeCode;
    private final ErrorCode errorCode;

    /**
     * @param placeCode unique identifier of the exception
     * @param errorCode error code to be returned to the user
     * @param message custom message to be logged internally
     */
    public ApiBusinessException(String placeCode, ErrorCode errorCode, String message) {
        super(message);
        this.placeCode = placeCode;
        this.errorCode = errorCode;
    }
}
