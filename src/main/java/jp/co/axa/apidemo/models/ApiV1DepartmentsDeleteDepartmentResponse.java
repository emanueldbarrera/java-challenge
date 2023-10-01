package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Response DTO for DELETE api/v1/departments/{departmentId}
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsDeleteDepartmentResponse extends BaseResponse {
    private ApiV1DepartmentEntityResponse department;
}
