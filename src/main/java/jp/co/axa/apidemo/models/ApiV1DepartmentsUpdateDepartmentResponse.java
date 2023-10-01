package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Response DTO for PUT api/v1/departments/{departmentId}
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsUpdateDepartmentResponse extends BaseResponse {
    @Nullable
    private ApiV1DepartmentEntityResponse department;
}
