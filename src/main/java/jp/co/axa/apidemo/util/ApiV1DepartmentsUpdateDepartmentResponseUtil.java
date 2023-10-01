package jp.co.axa.apidemo.util;

import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentEntityResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsUpdateDepartmentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiV1DepartmentsUpdateDepartmentResponseUtil {

    /**
     * Maps a {@link Department} to the corresponding DTO object
     *
     * @param department an instance of {@link Department}
     * @return {@link ApiV1DepartmentsUpdateDepartmentResponse}
     */
    public static ApiV1DepartmentsUpdateDepartmentResponse buildResponseSuccess(final Department department) {
        return ApiV1DepartmentsUpdateDepartmentResponse.builder()
                .department(ApiV1DepartmentEntityResponse.builder()
                        .id(department.getId())
                        .name(department.getName())
                        .build())
                .resultType(ResultType.SUCCESS.getCode())
                .build();
    }

    /**
     * Returns the corresponding response DTO object of an unsuccessful request
     *
     * @param errorCode
     * @param errorMessage
     * @return {@link ApiV1DepartmentsUpdateDepartmentResponse}
     */
    public static ApiV1DepartmentsUpdateDepartmentResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1DepartmentsUpdateDepartmentResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
