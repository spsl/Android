package xxe.restrant.order.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import xxe.restrant.order.domain.OrderInfo;
import xxe.restrant.utils.DBUtil;



public class OrderInfoServiceImpl implements OrderInfoService{

	
	///添加订单信息
	public boolean addOrderInfo(OrderInfo orderInfo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into t_orderInfo values (null,?,?,?,?,?) ";
			
		try {
			////链接数据库
			conn = DBUtil.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderInfo.getOrderName() );
			pstmt.setString(2, orderInfo.getUserName());
			pstmt.setString(3, orderInfo.getOrderAddr());
			pstmt.setString(4, orderInfo.getOrderPhoneNo());
			pstmt.setString(5, orderInfo.getOrderMessage());
			
			pstmt.executeUpdate();
			System.out.println("===========================update orderInfo");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}
}
