package xxx.api.dto.validator.address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class CoordsValidator implements ConstraintValidator<CoordsValidate, String> {
    public static final String LONGITUDE_MIN = "73.66";
    public static final String LONGITUDE_MAX = "135.05";
    public static final String LATITUDE_MIN = "3.86";
    public static final String LATITUDE_MAX = "53.55";

    @Override
    public void initialize(CoordsValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String[] poi = value.split(",");
        try {
            if (poi.length != 2) {
                throw new Exception("无法分割成经度和纬度");
            }
            double longitude = Double.valueOf(poi[0]), latitude = Double.valueOf(poi[1]);
            if (Double.valueOf(LONGITUDE_MIN).compareTo(longitude) > 0 || Double.valueOf(LONGITUDE_MAX).compareTo(longitude) < 0) {
                throw new Exception(String.format("longitude=%f, 之必须在%s-%s之间", longitude, LONGITUDE_MIN, LONGITUDE_MIN));
            }

            if (Double.valueOf(LATITUDE_MIN).compareTo(latitude) > 0 || Double.valueOf(LATITUDE_MAX).compareTo(latitude) < 0) {
                throw new Exception(String.format("latitude=%f, 之必须在%s-%s之间", latitude, LATITUDE_MIN, LATITUDE_MAX));
            }

            return true;
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("坐标系 %s 不合法，%s", value, e.getMessage()))
                    .addConstraintViolation();
            return false;
        }
    }
}
