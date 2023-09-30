package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Request DTO for GET api/v1/employees/:employeeId
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesGetEmployeeRequest extends BaseRequest {
    @NotNull
    @Positive
    private Long employeeId;
}
