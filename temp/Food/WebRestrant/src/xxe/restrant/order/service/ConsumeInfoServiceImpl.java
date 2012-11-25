package xxe.restrant.order.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import xxe.restrant.order.domain.ConsumeInfo;
import xxe.restrant.utils.DBUtil;




public class ConsumeInfoServiceImpl implements ConsumeInfoService  {

    ///根据用户ID添加消费历史记录信息
	public boolean addConsumeInfo(ConsumeInfo consumeInfo){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into t_consumeinfo values (null,?,?,?,?,?,?,?)";
			
		try {
			////链接数据库
			conn = DBUtil.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, consumeInfo.getUserId().intValue());
			pstmt.setInt(2, consumeInfo.getFoodId().intValue());
			pstmt.setString(3, consumeInfo.getFoodName());
			pstmt.setInt(4, consumeInfo.getFoodPrice().intValue());
			pstmt.setInt(5, consumeInfo.getFoodNum().intValue());
			pstmt.setInt(6, consumeInfo.getFoodTotalPrice().intValue());
			pstmt.setInt(7, consumeInfo.getFoodMark().intValue());

			
			pstmt.executeUpdate();
			System.out.println("===========================update consumeInfo");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	
	
}
