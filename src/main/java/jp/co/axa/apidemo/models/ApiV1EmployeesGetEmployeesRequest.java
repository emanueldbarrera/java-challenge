package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Request DTO for GET api/v1/employees
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesGetEmployeesRequest extends BaseRequest {
    @NotNull
    @Positive
    @Max(100)
    private Integer limit;
    @NotNull
    @PositiveOrZero
    private Integer offset;
}
