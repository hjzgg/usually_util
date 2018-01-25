package xxx.api.dto.validator.address;

import xxx.api.dto.shipment.Poi;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class PoiValidator implements ConstraintValidator<PoiValidate, Poi> {

    @Override
    public void initialize(PoiValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Poi value, ConstraintValidatorContext context) {
        return true;
    }
}
