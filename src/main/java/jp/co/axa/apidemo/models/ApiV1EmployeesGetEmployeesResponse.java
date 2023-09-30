package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Response DTO for GET api/v1/employees/
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesGetEmployeesResponse extends BaseResponse {
    private List<ApiV1EmployeeEntityResponse> employees;
}
