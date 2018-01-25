package xxx.api.dto.storage;

import java.util.Date;
import java.util.List;

/**
 * 物流单详情
 */
public class LogisticsOrderDto {

    /**
     * 物流单号
     */
    private String logisticsOrderCode;

    /**
     * 提示,新建网点有提示
     */
    private String prompt;
    /**
     * 城市name
     */
    private String cityName;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 网点code
     */
    private String shelfGroupCode;
    /**
     * 网点name
     */
    private String shelfGroupName;
    /**
     * 网点详细地址
     */
    private String shelfGroupAddress;
    /**
     * 网点日常联系人
     */
    private String contactName;
    /**
     * 网点日常联系人电话
     */
    private String contactPhone;
    /**
     * 1补货，2盘货加补货,3铺货
     */
    private Integer orderType;
    /**
     *
     */
    private String orderTypeStr;
    /**
     * 配送日期
     */
    private Date takeawayDate;
    /**
     * 配送日期
     */
    private String takeawayDateStr;
    /**
     * 货架描述
     */
    private String shelfDesc;
    /**
     * 备注
     */
    private String remark;

    /**
     * 补货详情
     */
    List<ReplenishmentOrderRepDto> replenishmentForExportList;

    public String getLogisticsOrderCode() {
        return logisticsOrderCode;
    }

    public void setLogisticsOrderCode(String logisticsOrderCode) {
        this.logisticsOrderCode = logisticsOrderCode;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getShelfGroupCode() {
        return shelfGroupCode;
    }

    public void setShelfGroupCode(String shelfGroupCode) {
        this.shelfGroupCode = shelfGroupCode;
    }

    public String getShelfGroupName() {
        return shelfGroupName;
    }

    public void setShelfGroupName(String shelfGroupName) {
        this.shelfGroupName = shelfGroupName;
    }

    public String getShelfGroupAddress() {
        return shelfGroupAddress;
    }

    public void setShelfGroupAddress(String shelfGroupAddress) {
        this.shelfGroupAddress = shelfGroupAddress;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }

    public Date getTakeawayDate() {
        return takeawayDate;
    }

    public void setTakeawayDate(Date takeawayDate) {
        this.takeawayDate = takeawayDate;
    }

    public String getTakeawayDateStr() {
        return takeawayDateStr;
    }

    public void setTakeawayDateStr(String takeawayDateStr) {
        this.takeawayDateStr = takeawayDateStr;
    }

    public String getShelfDesc() {
        return shelfDesc;
    }

    public void setShelfDesc(String shelfDesc) {
        this.shelfDesc = shelfDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ReplenishmentOrderRepDto> getReplenishmentForExportList() {
        return replenishmentForExportList;
    }

    public void setReplenishmentForExportList(List<ReplenishmentOrderRepDto> replenishmentForExportList) {
        this.replenishmentForExportList = replenishmentForExportList;
    }

    @Override
    public String toString() {
        return "LogisticsOrderDto{" +
                "logisticsOrderCode='" + logisticsOrderCode + '\'' +
                ", prompt='" + prompt + '\'' +
                ", cityName='" + cityName + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", shelfGroupCode='" + shelfGroupCode + '\'' +
                ", shelfGroupName='" + shelfGroupName + '\'' +
                ", shelfGroupAddress='" + shelfGroupAddress + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", orderType=" + orderType +
                ", orderTypeStr='" + orderTypeStr + '\'' +
                ", takeawayDate=" + takeawayDate +
                ", takeawayDateStr='" + takeawayDateStr + '\'' +
                ", shelfDesc='" + shelfDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", replenishmentForExportList=" + replenishmentForExportList +
                '}';
    }
}
