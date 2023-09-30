package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Response DTO for POST api/v1/employees
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesSaveEmployeeResponse extends BaseResponse {
    @Nullable
    private ApiV1EmployeeEntity employee;
}
