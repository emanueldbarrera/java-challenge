package jp.co.axa.apidemo.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Possible results for API calls
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultType {
    UNKNOWN(""),
    SUCCESS("success"),
    FAILURE("failure");

    @JsonValue
    private final String code;
}
