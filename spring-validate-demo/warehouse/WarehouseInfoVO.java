package xxx.api.dto.warehouse;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.address.AddressValidate;
import xxx.api.dto.validator.address.CoordsValidate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-10 下午3:04
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, WarehouseInfoVO.class})
public class WarehouseInfoVO {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.warehouseId.null}")
    @Min(value = 0, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseInfoVO.warehouseId.min}")
    private Long warehouseId;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.warehouseName.blank}")
    private String warehouseName;

    @Range(min = 1, max = 3, groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.warehouseType.range}")
    private byte warehouseType;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.location.blank}")
    @CoordsValidate(groups = ValidatorUtils.ValidatorGroup.Second.class)
    private String location;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.address.blank}")
    @AddressValidate(groups = ValidatorUtils.ValidatorGroup.Second.class)
    private String address;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.contactName.blank}")
    private String contactName;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.contactPhone.blank}")
    @Pattern(regexp = "(\\+\\d+)?1[34578]\\d{9}$", groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{warehouseInfoVO.contactPhone.pattern}")
    private String contactPhone;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.provinceName.blank}")
    private String provinceName;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.cityName.blank}")
    private String cityName;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.districtName.blank}")
    private String districtName;

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.company.blank}")
    private String company;

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.updateTime.null}")
    private Integer updateTime;

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.cityId.null}")
    private Integer cityId;

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.provinceId.null}")
    private Integer provinceId;

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{warehouseInfoVO.districtId.null}")
    private Integer districtId;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public byte getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(byte warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
