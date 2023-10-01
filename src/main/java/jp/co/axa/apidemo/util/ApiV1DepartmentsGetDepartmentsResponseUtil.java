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

    /**
     * Maps a {@link Department} to the corresponding DTO object
     *
     * @param departments a {@link List} of {@link Department}
     * @return {@link ApiV1DepartmentsGetDepartmentsResponse}
     */
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

    /**
     * Returns the corresponding response DTO object of an unsuccessful request
     *
     * @param errorCode
     * @param errorMessage
     * @return {@link ApiV1DepartmentsGetDepartmentsResponse}
     */
    public static ApiV1DepartmentsGetDepartmentsResponse buildResponseFailure(final ErrorCode errorCode, final String errorMessage) {
        return ApiV1DepartmentsGetDepartmentsResponse.builder()
                .resultType(ResultType.FAILURE.getCode())
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .build();
    }
}
