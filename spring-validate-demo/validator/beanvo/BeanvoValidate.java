package xxx.api.dto.validator.beanvo;

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
@Constraint(validatedBy = BeanvoValidator.class)
@Documented
public @interface BeanvoValidate {
    String message() default "beanvo.validate.invalid";

    String prefix();

    //分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}