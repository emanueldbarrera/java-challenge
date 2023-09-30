package jp.co.axa.apidemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Request DTO for PUT api/v1/employees/{employeeId}
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ApiV1EmployeesUpdateEmployeeRequest extends ApiV1EmployeeEntityRequest {
    @NotNull
    @Positive
    @JsonIgnore
    private Long employeeId;
}
