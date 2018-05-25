package thai.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains("@");
    }
}
