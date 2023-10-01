package jp.co.axa.apidemo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Base DTO class for all requests. It provides of params validation
 *
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseRequest {

    /**
     * Validates all the constraints configured for this object's attributes
     *
     * @throws ConstraintViolationException
     */
    public void validate() throws ConstraintViolationException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (validator != null) {
            Set<ConstraintViolation<BaseRequest>> violations = validator.validate(this);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }
}
