package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class ApiV1EmployeeEntityRequest extends BaseRequest {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 80)
    private String department;
    @NotNull
    @Positive
    private Integer salary;
}
