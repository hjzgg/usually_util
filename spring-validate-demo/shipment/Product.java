package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 商品详情
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, Product.class})
public class Product {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.skuId.null}")
    private Long skuId;//商品编号

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.name.blank}")
    private String name;//商品名称

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.amount.null}")
    @Min(value = 1, groups = ValidatorUtils.ValidatorGroup.Second.class)
    private Integer amount;//分配数量

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.netWeight.blank}")
    private String netWeight;//净含量，浮点

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.netWeightUnit.null}")
    @Range(min = 1, max = 4, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{product.netWeightUnit.range}")
    private Integer netWeightUnit;//净含量单位，枚举

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.specifications.null}")
    @Min(value = 1, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{product.specifications.min}")
    private Integer specifications;//规格数量（int）

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{product.upcUnit.null}")
    @Range(min = 1, max = 12, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{product.upcUnit.range}")
    private Integer upcUnit;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getNetWeightUnit() {
        return netWeightUnit;
    }

    public void setNetWeightUnit(Integer netWeightUnit) {
        this.netWeightUnit = netWeightUnit;
    }

    public Integer getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Integer specifications) {
        this.specifications = specifications;
    }

    public Integer getUpcUnit() {
        return upcUnit;
    }

    public void setUpcUnit(Integer upcUnit) {
        this.upcUnit = upcUnit;
    }
}
