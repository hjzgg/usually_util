package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.shipment.ShipmentValidate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 签收详情
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ReceiptProduct.class, ReceiptProductsInfo.class})
public class ReceiptProductsInfo {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{receiptProductsInfo.shipmentId.null}")
    @ShipmentValidate(groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{receiptProductsInfo.shipmentId.exist}")
    private Long shipmentId;//运单号

    @NotEmpty(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{receiptProductsInfo.receiptProducts.empty}")
    @Valid
    private List<ReceiptProduct> receiptProducts;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public List<ReceiptProduct> getReceiptProducts() {
        return receiptProducts;
    }

    public void setReceiptProducts(List<ReceiptProduct> receiptProducts) {
        this.receiptProducts = receiptProducts;
    }
}