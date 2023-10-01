package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Request DTO for GET api/v1/departments
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsGetDepartmentsResponse extends BaseResponse {
    private List<ApiV1DepartmentEntityResponse> departments;
}
