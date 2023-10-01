package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentsResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1DepartmentsGetDepartmentsResponseUtil {
    public static ApiV1DepartmentsGetDepartmentsResponse buildResponseSuccess(final List<Department> departments) {
        final List<ApiV1DepartmentEntityResponse> departmentsResponses = new ArrayList<>();
        for (Department department : departments) {
            departmentsResponses.add(ApiV1DepartmentEntityResponse.builder()
                    .id(department.getId())
                    .name(department.getName())
                    .build());
        }

        return ApiV1DepartmentsGetDepartmentsResponse.builder()
                .departments(departmentsResponses)
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1DepartmentsGetDepartmentsResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1DepartmentsGetDepartmentsResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
