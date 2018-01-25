package xxx.api.dto.validator.address;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
//指定验证器  
@Constraint(validatedBy = PoiValidator.class)
@Documented
public @interface PoiValidate {
    String message() default "{address.validate.invalid}";
    //分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}