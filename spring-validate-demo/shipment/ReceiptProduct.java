package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 签收商品
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ReceiptProduct.class})
public class ReceiptProduct {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{receiptProduct.skuId.null}")
    private Long skuId;//商品编号

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{receiptProduct.receiptAmount.null}")
    @Min(value = 0, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{receiptProduct.receiptAmount.min}")
    private Integer receiptAmount;//签收数量


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(Integer receiptAmount) {
        this.receiptAmount = receiptAmount;
    }
}