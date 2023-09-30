package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Request DTO for api/v1/employees/:employeeId
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesGetEmployeeResponse extends BaseResponse {
    @Nullable
    private Long employeeId;
    @Nullable
    private String name;
    @Nullable
    private String department;
    @Nullable
    private Integer salary;
}
