package jp.co.axa.apidemo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Builder
@AllArgsConstructor
public class ApiV1EmployeesGetEmployeesEntity {
    @Nullable
    private Long employeeId;
    @Nullable
    private String name;
    @Nullable
    private String department;
    @Nullable
    private Integer salary;
}
