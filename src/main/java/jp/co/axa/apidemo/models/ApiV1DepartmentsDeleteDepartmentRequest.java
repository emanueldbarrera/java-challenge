package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Request DTO for DELETE api/v1/departments/{departmentId}
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsDeleteDepartmentRequest extends BaseRequest {
    @NotNull
    @Positive
    private Long departmentId;
}
