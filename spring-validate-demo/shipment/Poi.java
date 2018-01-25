package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.address.CoordsValidator;
import xxx.api.dto.validator.address.PoiValidate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 位置
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ValidatorUtils.ValidatorGroup.Third.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, ValidatorUtils.ValidatorGroup.Third.class, Poi.class})
@PoiValidate(groups = ValidatorUtils.ValidatorGroup.Third.class)
public class Poi {
    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.longitude.null}")
    @DecimalMin(value = CoordsValidator.LONGITUDE_MIN, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{poi.longitude.min}")
    @DecimalMax(value = CoordsValidator.LONGITUDE_MAX, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{poi.longitude.max}")
    private Double longitude;//经度

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.latitude.null}")
    @DecimalMin(value = CoordsValidator.LATITUDE_MIN, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{poi.latitude.min}")
    @DecimalMax(value = CoordsValidator.LATITUDE_MAX, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{poi.latitude.max}")
    private Double latitude;//纬度

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.address.blank}")
    private String address;//详细地址

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.provinceName.blank}")
    private String provinceName;//一级地址（省）

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.provinceId.null}")
    @Min(value = 0, groups = ValidatorUtils.ValidatorGroup.Second.class)
    private Integer provinceId;//一级地址（省）

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.cityName.blank}")
    private String cityName;//二级地址（城市）

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.cityId.null}")
    @Min(value = 0, groups = ValidatorUtils.ValidatorGroup.Second.class)
    private Integer cityId;//二级地址（城市）

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.districtName.blank}")
    private String districtName;//三级地址（区）

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.districtId.null}")
    @Min(value = 0, groups = ValidatorUtils.ValidatorGroup.Second.class)
    private Integer districtId;//三级地址（区）

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{poi.coordsType.null}")
    @Range(min = 1, max = 2, groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{poi.coordsType.range}")
    private Integer coordsType;//坐标系类型(1 火星（腾讯，高德）、 2 百度)

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCoordsType() {
        return coordsType;
    }

    public void setCoordsType(Integer coordsType) {
        this.coordsType = coordsType;
    }

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
}