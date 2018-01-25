package xxx.api.dto.shipment;

import xxx.api.dto.validator.ValidatorUtils;
import xxx.api.dto.validator.beanvo.BeanvoValidate;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-03 下午2:41
 **/
@Validated({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class})
@GroupSequence({ValidatorUtils.ValidatorGroup.First.class, ValidatorUtils.ValidatorGroup.Second.class, UserInfo.class})
public class UserInfo {
    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{userInfo.npName.blank}")
    private String npName;//网点名称

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{userInfo.npId.null}")
    private Long npId;//网点ID

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{userInfo.contactName.blank}")
    private String contactName;//联系人姓名

    @NotBlank(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{userInfo.contactPhone.blank}")
    @Pattern(regexp = "(\\+\\d+)?1[34578]\\d{9}$", groups = ValidatorUtils.ValidatorGroup.Second.class, message = "{userInfo.contactPhone.pattern}")
    private String contactPhone;//联系人电话

    @NotNull(groups = ValidatorUtils.ValidatorGroup.First.class, message = "{userInfo.poi.null}")
    @BeanvoValidate(prefix = "userInfo", groups = ValidatorUtils.ValidatorGroup.Second.class)
    private Poi poi;//地址信息

    public String getNpName() {
        return npName;
    }

    public void setNpName(String npName) {
        this.npName = npName;
    }

    public Long getNpId() {
        return npId;
    }

    public void setNpId(Long npId) {
        this.npId = npId;
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

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }
}