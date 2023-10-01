package jp.co.axa.apidemo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Base DTO class for all responses. It enforces the provision of resultType,
 * and optionally of error information if the call result was unsuccessful
 *
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse {
    private String resultType;
    @Nullable
    private String errorCode;
    @Nullable
    private String errorMessage;
}
