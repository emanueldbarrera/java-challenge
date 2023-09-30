package jp.co.axa.apidemo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jp.co.axa.apidemo.common.ResultType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

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
