package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeeEntity;
import jp.co.axa.apidemo.models.ApiV1EmployeesDeleteEmployeeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1EmployeesDeleteEmployeeResponseUtil {
    public static ApiV1EmployeesDeleteEmployeeResponse buildResponseSuccess(final Employee employee) {
        return ApiV1EmployeesDeleteEmployeeResponse.builder()
                .employee(ApiV1EmployeeEntity.builder()
                        .employeeId(employee.getId())
                        .name(employee.getName())
                        .department(employee.getDepartment())
                        .salary(employee.getSalary())
                        .build())
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1EmployeesDeleteEmployeeResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1EmployeesDeleteEmployeeResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
