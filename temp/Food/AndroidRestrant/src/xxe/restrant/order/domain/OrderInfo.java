package xxe.restrant.order.domain;

////�û�������Ϣ
public class OrderInfo {
    ////�û����ID
	private Integer orderID;
	///�����û���
	private String orderName;
	///�ͻ���¼�û���  ���  �����ֶ�
	private String userName;
	///������ַ
	private String orderAddr;
	//������ϵ�绰
	private String orderPhoneNo;
	//��������
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
