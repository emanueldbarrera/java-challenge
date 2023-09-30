package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1EmployeesGetEmployeeResponseUtil {

    public static ApiV1EmployeesGetEmployeeResponse buildResponseSuccess(final Employee employee) {
        return ApiV1EmployeesGetEmployeeResponse.builder()
                .employeeId(employee.getId())
                .name(employee.getName())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1EmployeesGetEmployeeResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1EmployeesGetEmployeeResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
