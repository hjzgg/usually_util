package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.shipment.ShipmentValidate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 出库详情
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, StockoutProductsInfo.class})
public class StockoutProductsInfo extends ReceiptProductsInfo{
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{stockoutProductsInfo.stockoutTime.null}")
    private Long stockoutTime;//出库时间

    public Long getStockoutTime() {
        return stockoutTime;
    }

    public void setStockoutTime(Long stockoutTime) {
        this.stockoutTime = stockoutTime;
    }
}