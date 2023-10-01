package jp.co.axa.apidemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Request DTO for PUT api/v1/departments/{departmentId}
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ApiV1DepartmentsUpdateDepartmentRequest extends BaseRequest {
    @NotNull
    @Positive
    @JsonIgnore
    private Long id;
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;
}
