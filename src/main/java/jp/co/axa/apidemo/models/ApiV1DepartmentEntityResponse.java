package jp.co.axa.apidemo.models;

import jp.co.axa.apidemo.entities.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * DTO model for a {@link Department}
 */
@Getter
@Builder
@AllArgsConstructor
public class ApiV1DepartmentEntityResponse {
    @Nullable
    private Long id;
    @Nullable
    private String name;
}
