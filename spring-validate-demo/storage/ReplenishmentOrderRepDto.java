package xxx.api.dto.storage;

/**
 * 补货详情
 */
public class ReplenishmentOrderRepDto {

    /**
     * 物流单号
     */
    private String logisticsOrderNo;

    /**
     *sku业务标识
     */
    private Long skuId;

    /**
     * 售卖id
     */
    private Long commoditySaleId;

    /**
     * 名称
     */
    private String commodityName;
    /**
     * 商品图片url
     */
    private String commodityImgUrl;
    /**
     * 货架id
     */
    private Long shelfId;
    /**
     * 货架code
     */
    private String shelfCode;
    /**
     * 货架name
     */
    private String shelfName;
    /**
     * 货架类型,0货架，1冷藏柜
     */
    private Integer shelfType;
    /**
     * 货架类型,0货架，1冷藏柜
     */
    private String shelfTypeName;
    /**
     * 补货状态，0已创建，10补货完成
     */
    private Integer replenishStatus;
    /**
     * 分层字典值,1:1层...2:2层....-1:挂层
     */
    private Integer storey;
    /**
     * 分层字典值,1:1层...2:2层....-1:挂层
     */
    private String storeyStr;
    /**
     * 货架坐标位置
     */
    private Integer location;
    /**
     * 仓库库存状态，0充足，10不充足
     */
    private Integer stockEnough;
    /**
     * 系统请求应补数量
     */
    private Integer sysRequireReplenishNum;
    /**
     * 应补数量，库房实际拥有数量
     */
    private Integer requireReplenishNum;
    /**
     * 实补数量
     */
    private Integer replenishNum;
    /**
     * 补货后可售库存
     */
    private Integer replenishedStockNum;
    /**
     * 最大库存
     */
    private Integer stockMaxNum;

    public String getLogisticsOrderNo() {
        return logisticsOrderNo;
    }

    public void setLogisticsOrderNo(String logisticsOrderNo) {
        this.logisticsOrderNo = logisticsOrderNo;
    }

    public Long getCommoditySaleId() {
        return commoditySaleId;
    }

    public void setCommoditySaleId(Long commoditySaleId) {
        this.commoditySaleId = commoditySaleId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityImgUrl() {
        return commodityImgUrl;
    }

    public void setCommodityImgUrl(String commodityImgUrl) {
        this.commodityImgUrl = commodityImgUrl;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public Integer getShelfType() {
        return shelfType;
    }

    public void setShelfType(Integer shelfType) {
        this.shelfType = shelfType;
    }

    public String getShelfTypeName() {
        return shelfTypeName;
    }

    public void setShelfTypeName(String shelfTypeName) {
        this.shelfTypeName = shelfTypeName;
    }

    public Integer getReplenishStatus() {
        return replenishStatus;
    }

    public void setReplenishStatus(Integer replenishStatus) {
        this.replenishStatus = replenishStatus;
    }

    public Integer getStorey() {
        return storey;
    }

    public void setStorey(Integer storey) {
        this.storey = storey;
    }

    public String getStoreyStr() {
        return storeyStr;
    }

    public void setStoreyStr(String storeyStr) {
        this.storeyStr = storeyStr;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getStockEnough() {
        return stockEnough;
    }

    public void setStockEnough(Integer stockEnough) {
        this.stockEnough = stockEnough;
    }

    public Integer getSysRequireReplenishNum() {
        return sysRequireReplenishNum;
    }

    public void setSysRequireReplenishNum(Integer sysRequireReplenishNum) {
        this.sysRequireReplenishNum = sysRequireReplenishNum;
    }

    public Integer getRequireReplenishNum() {
        return requireReplenishNum;
    }

    public void setRequireReplenishNum(Integer requireReplenishNum) {
        this.requireReplenishNum = requireReplenishNum;
    }

    public Integer getReplenishNum() {
        return replenishNum;
    }

    public void setReplenishNum(Integer replenishNum) {
        this.replenishNum = replenishNum;
    }

    public Integer getReplenishedStockNum() {
        return replenishedStockNum;
    }

    public void setReplenishedStockNum(Integer replenishedStockNum) {
        this.replenishedStockNum = replenishedStockNum;
    }

    public Integer getStockMaxNum() {
        return stockMaxNum;
    }

    public void setStockMaxNum(Integer stockMaxNum) {
        this.stockMaxNum = stockMaxNum;
    }

    @Override
    public String toString() {
        return "ReplenishmentOrderRepDto{" +
                "logisticsOrderNo='" + logisticsOrderNo + '\'' +
                ", commoditySaleId=" + commoditySaleId +
                ", skuId=" + skuId +
                ", commodityName='" + commodityName + '\'' +
                ", commodityImgUrl='" + commodityImgUrl + '\'' +
                ", shelfId=" + shelfId +
                ", shelfCode='" + shelfCode + '\'' +
                ", shelfName='" + shelfName + '\'' +
                ", shelfType=" + shelfType +
                ", shelfTypeName='" + shelfTypeName + '\'' +
                ", replenishStatus=" + replenishStatus +
                ", storey=" + storey +
                ", storeyStr='" + storeyStr + '\'' +
                ", location=" + location +
                ", stockEnough=" + stockEnough +
                ", sysRequireReplenishNum=" + sysRequireReplenishNum +
                ", requireReplenishNum=" + requireReplenishNum +
                ", replenishNum=" + replenishNum +
                ", replenishedStockNum=" + replenishedStockNum +
                ", stockMaxNum=" + stockMaxNum +
                '}';
    }
}
