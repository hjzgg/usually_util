package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.beanvo.BeanvoValidate;
import xxx.api.dto.validator.date.DateValidate;
import xxx.api.dto.validator.json.JsonValidate;
import xxx.api.dto.validator.logic.LogicValidate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 运单信息
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:35
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ValidatorUtils.ValidatorGroup.Third.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ValidatorUtils.ValidatorGroup.Third.class, WarehouseRequest.class})
@LogicValidate(groups = ValidatorUtils.ValidatorGroup.Third.class)
public class WarehouseRequest {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.warehouseId.null}")
    private Long warehouseId;//仓库信息【长整型】

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.businessType.null}")
    @Range(min = 1, max = 3, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseRequest.businessType.range}")
    private Integer businessType;//业态类型(1,无人货架、2,门店、3,前置仓)【整型】

    @Range(min = 0, max = 3, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseRequest.businessSubType.range}")
    private Integer businessSubType = 0;//业态子类型（1,补货、2铺货、3盘货+补货）,仅在订单类型为无人货架【整型】

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.businessJson.blank}")
    @JsonValidate(groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseRequest.businessJson.jsonValidate}")
    private String businessJson;//货架和商品的对应规则【JSON字符串】

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.orderRemark.blank}")
    private String orderRemark;//业态订单备注【字符串】

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.orderNumber.blank}")
    private String orderNumber;//业态订单号（货架端的物流号）【字符串】

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.stockoutNumber.blank}")
    private String stockoutNumber;//出库单号【字符串】

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.requireReceiveStartTime.null}")
    @DateValidate(groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseRequest.requireReceiveStartTime.dateValidate}")
    private Long requireReceiveStartTime;//期望配送时间,毫秒数,起止时间【长整型】

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.requireReceiveEndTime.null}")
    @DateValidate(groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseRequest.requireReceiveEndTime.dateValidate}")
    private Long requireReceiveEndTime;//期望配送时间,毫秒数,起止时间【长整型】

    @NotEmpty(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.products.empty}")
    @BeanvoValidate(prefix = "warehouseRequest", groups = ValidatorUtils.ValidatorGroup.Second.class)
    private List<Product> products;//运单包含的商品信息

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseRequest.userInfo.null}")
    @BeanvoValidate(prefix = "warehouseRequest", groups = ValidatorUtils.ValidatorGroup.Second.class)
    private UserInfo userInfo;//客户信息

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getBusinessSubType() {
        return businessSubType;
    }

    public void setBusinessSubType(Integer businessSubType) {
        this.businessSubType = businessSubType;
    }

    public String getBusinessJson() {
        return businessJson;
    }

    public void setBusinessJson(String businessJson) {
        this.businessJson = businessJson;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStockoutNumber() {
        return stockoutNumber;
    }

    public void setStockoutNumber(String stockoutNumber) {
        this.stockoutNumber = stockoutNumber;
    }

    public Long getRequireReceiveStartTime() {
        return requireReceiveStartTime;
    }

    public void setRequireReceiveStartTime(Long requireReceiveStartTime) {
        this.requireReceiveStartTime = requireReceiveStartTime;
    }

    public Long getRequireReceiveEndTime() {
        return requireReceiveEndTime;
    }

    public void setRequireReceiveEndTime(Long requireReceiveEndTime) {
        this.requireReceiveEndTime = requireReceiveEndTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

