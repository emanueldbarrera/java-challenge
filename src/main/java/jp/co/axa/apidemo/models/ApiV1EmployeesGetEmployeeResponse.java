package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Response DTO for GET api/v1/employees/{employeeId}
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesGetEmployeeResponse extends BaseResponse {
    @Nullable
    private ApiV1EmployeeEntityResponse employee;
}
