package jp.co.axa.apidemo.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNKNOWN(""),
    INVALID_REQUEST_PARAMETER("invalid_request_parameter"),
    UNACCEPTABLE_REQUEST("unacceptable_request"),
    SYSTEM_ERROR("system_error"),
    AUTHENTICATION_FAILURE("authentication_failure"),
    UNAUTHORIZED_ACCESS("unauthorized_access"),
    NOT_FOUND("not_found");

    @JsonValue
    private final String code;
}
