package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeeEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeesResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1EmployeesGetEmployeesResponseUtil {
    public static ApiV1EmployeesGetEmployeesResponse buildResponseSuccess(final List<Employee> employees) {
        final List<ApiV1EmployeeEntityResponse> employeesResponses = new ArrayList<>();
        for (Employee employee : employees) {
            employeesResponses.add(ApiV1EmployeeEntityResponse.builder()
                    .employeeId(employee.getId())
                    .name(employee.getName())
                    .department(ApiV1DepartmentEntityResponse.builder()
                            .id(employee.getDepartment().getId())
                            .name(employee.getDepartment().getName())
                            .build())
                    .salary(employee.getSalary())
                    .build());
        }

        return ApiV1EmployeesGetEmployeesResponse.builder()
                .employees(employeesResponses)
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1EmployeesGetEmployeesResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1EmployeesGetEmployeesResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
