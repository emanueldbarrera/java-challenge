package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Request DTO for POST api/v1/departments
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1DepartmentsSaveDepartmentRequest extends BaseRequest {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;
}
