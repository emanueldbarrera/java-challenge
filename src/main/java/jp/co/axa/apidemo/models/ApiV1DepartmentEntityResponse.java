package jp.co.axa.apidemo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Builder
@AllArgsConstructor
public class ApiV1DepartmentEntityResponse {
    @Nullable
    private Long id;
    @Nullable
    private String name;
}
