package xxx.api.dto.validator.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class DateValidator implements ConstraintValidator<DateValidate, Long> {

    @Override
    public void initialize(DateValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        LocalDateTime curLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(value),
                TimeZone.getDefault().toZoneId());

        LocalDateTime theDayLocalDateTime = LocalDate.now().atStartOfDay();
        return curLocalDateTime.isAfter(theDayLocalDateTime);
    }
}
