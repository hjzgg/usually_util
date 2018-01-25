package xxx.api.dto.validator.logic;

import me.ele.scm.tms.base.api.WarehouseService;
import me.ele.scm.tms.common.model.model.ShipmentBizSubtype;
import me.ele.scm.tms.common.model.model.ShipmentBizType;
import xxx.api.dto.shipment.WarehouseRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-05 下午5:34
 **/
public class LogicValidator implements ConstraintValidator<LogicValidate, WarehouseRequest> {

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public void initialize(LogicValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(WarehouseRequest value, ConstraintValidatorContext context) {
        try {
            //仓库信息校验
            Long warehouseId = value.getWarehouseId();
            if (!warehouseService.findWarehouseById(warehouseId).isPresent()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(String.format("warehouseId=%d，仓库id不存在", warehouseId))
                        .addPropertyNode("warehouseId")
                        .addConstraintViolation();
                return false;
            }

            //业务类型校验（业务类型为货架，子业务类型才会有值）
            ShipmentBizType bizType = ShipmentBizType.getLabelByCode(value.getBusinessType().byteValue());
            if (!(bizType == ShipmentBizType.SHELF && ShipmentBizSubtype.getLabelByCode(value.getBusinessSubType().byteValue()) != ShipmentBizSubtype.DEFAULT
                    || bizType != ShipmentBizType.SHELF && ShipmentBizSubtype.getLabelByCode(value.getBusinessSubType().byteValue()) == ShipmentBizSubtype.DEFAULT)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(String.format("businessType=%d, businessSubType=%d, 业务类型和子业务类型不匹配", value.getBusinessType(), value.getBusinessSubType()))
                        .addPropertyNode("businessType||businessSubType")
                        .addConstraintViolation();
                return false;
            }

            //时间交验
            Long beginTime = value.getRequireReceiveStartTime();
            Long endTime = value.getRequireReceiveEndTime();
            if (beginTime > endTime) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(String.format("requireReceiveStartTime=%d， requireReceiveEndTime=%d，开始时间大于结束时间", beginTime, endTime))
                        .addPropertyNode("requireReceiveStartTime||requireReceiveEndTime")
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("运单信息校验异常->%s", e.getMessage()))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
