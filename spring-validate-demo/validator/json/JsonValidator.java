package xxx.api.dto.validator.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class JsonValidator implements ConstraintValidator<JsonValidate, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initialize(JsonValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            objectMapper.readTree(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
