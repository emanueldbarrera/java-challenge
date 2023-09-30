package jp.co.axa.apidemo.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultType {
    UNKNOWN(""),
    SUCCESS("success"),
    FAILURE("failure");

    @JsonValue
    private final String code;
}
