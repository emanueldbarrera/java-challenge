package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Response DTO for POST api/v1/departments
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsSaveDepartmentResponse extends BaseResponse {
    private ApiV1DepartmentEntityResponse department;
}
