package jp.co.axa.apidemo.models;

import jp.co.axa.apidemo.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * DTO model for a {@link Employee}
 */
@Getter
@Builder
@AllArgsConstructor
public class ApiV1EmployeeEntityResponse {
    @Nullable
    private Long employeeId;
    @Nullable
    private String name;
    @Nullable
    private ApiV1DepartmentEntityResponse department;
    @Nullable
    private Integer salary;
}
