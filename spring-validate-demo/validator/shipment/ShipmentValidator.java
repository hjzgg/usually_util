package xxx.api.dto.validator.shipment;

import me.ele.scm.tms.shipment.api.ShipmentService;
import me.ele.scm.tms.shipment.api.dto.ShipmentDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class ShipmentValidator implements ConstraintValidator<ShipmentValidate, Long> {

    @Autowired
    private ShipmentService shipmentService;

    @Override
    public void initialize(ShipmentValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        try {
            ShipmentDetail shipmentDetail = shipmentService.detail(value);
            if (Objects.isNull(shipmentDetail)) {
                return false;
            }
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("shipmentId=%d，校验时获取订单详情异常->%s", value, e.getMessage()))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}