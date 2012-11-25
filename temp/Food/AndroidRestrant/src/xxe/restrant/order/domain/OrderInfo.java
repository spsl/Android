package xxe.restrant.order.domain;

////用户订单信息
public class OrderInfo {
    ////用户编号ID
	private Integer orderID;
	///订单用户名
	private String orderName;
	///客户登录用户名  外键  关联字段
	private String userName;
	///订单地址
	private String orderAddr;
	//订单联系电话
	private String orderPhoneNo;
	//订单附言
	private String orderMessage;

	
	public OrderInfo() {
	}


	public OrderInfo(Integer orderID, String orderName, String userName,
			String orderAddr, String orderPhoneNo, String orderMessage) {
		this.orderID = orderID;
		this.orderName = orderName;
		this.userName = userName;
		this.orderAddr = orderAddr;
		this.orderPhoneNo = orderPhoneNo;
		this.orderMessage = orderMessage;
	}


	public String toString() {
		return "OrderInfo [orderAddr=" + orderAddr + ", orderID=" + orderID
				+ ", orderMessage=" + orderMessage + ", orderName=" + orderName
				+ ", orderPhoneNo=" + orderPhoneNo + ", userName=" + userName
				+ "]";
	}

	
}
