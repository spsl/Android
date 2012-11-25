package xxe.restrant.order.domain;

public class ConsumeInfo {
	///消费记录编号
	private Integer consumeId;
	///用户编号
	private Integer userId;
	///食品编号
	private Integer foodId;
	///食品名称
	private String foodName;
	//食品单价
	private Integer foodPrice;
   ///食品数量
	private Integer foodNum;
	///食品总金额
	private Integer foodTotalPrice;
	///食品评分
	private Integer foodMark;
	public Integer getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(Integer consumeId) {
		this.consumeId = consumeId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getFoodId() {
		return foodId;
	}
	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public Integer getFoodPrice() {
		return foodPrice;
	}
	public void setFoodPrice(Integer foodPrice) {
		this.foodPrice = foodPrice;
	}
	public Integer getFoodNum() {
		return foodNum;
	}
	public void setFoodNum(Integer foodNum) {
		this.foodNum = foodNum;
	}
	public Integer getFoodTotalPrice() {
		return foodTotalPrice;
	}
	public void setFoodTotalPrice(Integer foodTotalPrice) {
		this.foodTotalPrice = foodTotalPrice;
	}
	public Integer getFoodMark() {
		return foodMark;
	}
	public void setFoodMark(Integer foodMark) {
		this.foodMark = foodMark;
	}
	

}
