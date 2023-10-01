package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsDeleteDepartmentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1DepartmentsDeleteDepartmentResponseUtil {
    public static ApiV1DepartmentsDeleteDepartmentResponse buildResponseSuccess(final Department department) {
        return ApiV1DepartmentsDeleteDepartmentResponse.builder()
                .department(ApiV1DepartmentEntityResponse.builder()
                        .id(department.getId())
                        .name(department.getName())
                        .build())
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    public static ApiV1DepartmentsDeleteDepartmentResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1DepartmentsDeleteDepartmentResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
