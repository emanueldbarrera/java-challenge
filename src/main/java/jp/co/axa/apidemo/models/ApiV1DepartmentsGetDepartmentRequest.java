package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Request DTO for GET api/v1/departments/{departmentId}
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsGetDepartmentRequest extends BaseRequest {
    @NotNull
    @Positive
    private Long id;
}
