package xxe.restrant.order.domain;



////�û�������Ϣ
public class OrderInfo {
    ////�û����ID
	private Integer orderID;
	///�����û���
	private String orderName;
	///�ͻ���¼�û�ID  ���  �����ֶ�
	private String userName;
	///������ַ
	private String orderAddr;
	//������ϵ�绰
	private String orderPhoneNo;
	//��������
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
