package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeeEntityResponse;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1EmployeesSaveEmployeeResponseUtil {

    /**
     * Maps a {@link Employee} to the corresponding DTO object
     *
     * @param employee an instance of {@link Employee}
     * @return {@link ApiV1EmployeesSaveEmployeeResponse}
     */
    public static ApiV1EmployeesSaveEmployeeResponse buildResponseSuccess(final Employee employee) {
        return ApiV1EmployeesSaveEmployeeResponse.builder()
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

    /**
     * Returns the corresponding response DTO object of an unsuccessful request
     *
     * @param errorCode
     * @param errorMessage
     * @return {@link ApiV1EmployeesSaveEmployeeResponse}
     */
    public static ApiV1EmployeesSaveEmployeeResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1EmployeesSaveEmployeeResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
