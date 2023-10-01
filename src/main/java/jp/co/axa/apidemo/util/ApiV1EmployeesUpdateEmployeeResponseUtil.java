package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeeEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeesUpdateEmployeeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1EmployeesUpdateEmployeeResponseUtil {
    public static ApiV1EmployeesUpdateEmployeeResponse buildResponseSuccess(final Employee employee) {
        return ApiV1EmployeesUpdateEmployeeResponse.builder()
                .employee(ApiV1EmployeeEntityResponse.builder()
                        .employeeId(employee.getId())
                        .name(employee.getName())
                        .department(ApiV1DepartmentEntityResponse.builder()
                                .id(employee.getDepartment().getId())
                                .name(employee.getDepartment().getName())
                                .build())
                        .salary(employee.getSalary())
                        .build())
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1EmployeesUpdateEmployeeResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1EmployeesUpdateEmployeeResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
