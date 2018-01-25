package xxx.api.dto.storage;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.shipment.ShipmentValidate;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-11 下午2:17
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, CancelInfo.class})
public class CancelInfo {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{CancelInfo.shipmentId.null}")
    @ShipmentValidate(groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{CancelInfo.shipmentId.exist}")
    private Long shipmentId;

    private String message;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
