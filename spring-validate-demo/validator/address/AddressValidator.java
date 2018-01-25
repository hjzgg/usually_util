package xxx.api.dto.validator.address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class AddressValidator implements ConstraintValidator<AddressValidate, String> {
    @Override
    public void initialize(AddressValidate constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
