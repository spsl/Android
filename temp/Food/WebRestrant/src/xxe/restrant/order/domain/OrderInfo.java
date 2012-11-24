package xxe.restrant.order.domain;



////用户订单信息
public class OrderInfo {
    ////用户编号ID
	private Integer orderID;
	///订单用户名
	private String orderName;
	///客户登录用户ID  外键  关联字段
	private String userName;
	///订单地址
	private String orderAddr;
	//订单联系电话
	private String orderPhoneNo;
	//订单附言
	private String orderMessage;
	public Integer getOrderID() {
		return orderID;
	}
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderAddr() {
		return orderAddr;
	}
	public void setOrderAddr(String orderAddr) {
		this.orderAddr = orderAddr;
	}
	public String getOrderPhoneNo() {
		return orderPhoneNo;
	}
	public void setOrderPhoneNo(String orderPhoneNo) {
		this.orderPhoneNo = orderPhoneNo;
	}
	public String getOrderMessage() {
		return orderMessage;
	}
	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	} 
	
	
	
}
