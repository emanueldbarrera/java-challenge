package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Request DTO for POST api/v1/employees
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ApiV1EmployeesSaveEmployeeRequest extends ApiV1EmployeeEntityRequest {
}
