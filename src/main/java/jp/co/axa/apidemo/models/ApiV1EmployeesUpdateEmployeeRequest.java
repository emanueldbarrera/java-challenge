package jp.co.axa.apidemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Request DTO for PUT api/v1/employees/{employeeId}
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ApiV1EmployeesUpdateEmployeeRequest extends BaseRequest {
    @NotNull
    @Positive
    @JsonIgnore
    private Long employeeId;
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;
    @Positive
    private Long departmentId;
    @Positive
    private Integer salary;
}
