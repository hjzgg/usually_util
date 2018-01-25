package xxx.api.dto.validator.beanvo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import xxx.api.dto.validator.ValidatorUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class BeanvoValidator implements ConstraintValidator<BeanvoValidate, Object> {

    private String prefix;

    @Override
    public void initialize(BeanvoValidate constraintAnnotation) {
        prefix = constraintAnnotation.prefix();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Optional<String> result = ValidatorUtils.validateResultProcess(value);
            if (result.isPresent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                context.disableDefaultConstraintViolation();
                List<ValidatorUtils.ErrorMessage> errorMessages = objectMapper.readValue(result.get(), new TypeReference<List<ValidatorUtils.ErrorMessage>>(){});
                for (ValidatorUtils.ErrorMessage errorMessage : errorMessages) {
                    errorMessage.setPropertyPath(String.format("%s.%s", prefix, errorMessage.getPropertyPath()));
                }
                context.buildConstraintViolationWithTemplate(objectMapper.writeValueAsString(errorMessages)).addConstraintViolation();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
