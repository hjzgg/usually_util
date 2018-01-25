package xxx.api.dto.validator.date;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
//指定验证器  
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface DateValidate {
    String message() default "date.validate.invalid";
    //分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}