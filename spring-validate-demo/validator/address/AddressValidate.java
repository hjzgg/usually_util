package xxx.api.dto.validator.address;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-10 下午3:42
 **/
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
//指定验证器
@Constraint(validatedBy = AddressValidator.class)
@Documented
public @interface AddressValidate {
    String message() default "{coords.validate.invalid}";
    //分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}