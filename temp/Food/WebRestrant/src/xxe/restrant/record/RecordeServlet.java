package xxe.restrant.record;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxe.restrant.utils.DBUtil;
/**
 * 历史记录模块功能的实现
 * @author ASUS
 *此类实现了历史记录的查询，评分，删除
 */
public class RecordeServlet extends HttpServlet {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private List data = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
//		response.setCharacterEncoding("utf-8");
		/**
		 * 获取动作参数如果为record，就执行查询操作
		 */
		if(request.getParameter("action").equals("record")){
			String userId = request.getParameter("userId");
			String result = queryRecord(userId);
			System.out.println(result);
//			out.print(new String(result.getBytes("iso-8859-1"), "utf-8"));
			out.println(result);
		}
		if(request.getParameter("action").equals("delete")){
			String consumeId = request.getParameter("consumeId");
			 deleteRecord(consumeId);
		}
		if(request.getParameter("action").equals("update")){
			String consumeId = request.getParameter("consumeId");
			String foodMark = request.getParameter("foodMark");
			update(foodMark,consumeId );
		}

		
	}

	public String queryRecord(String userId){
//		String userId  = request.getParameter("userId");
		String sql = "select consumeId, foodName,foodPrice,foodNum,foodTotprice ,foodMark from t_consumeinfo where userId = ?";
		StringBuffer sb = new StringBuffer("");
		try {
			conn = DBUtil.getConnection();
			pstmt = DBUtil.getPreparedStatement(conn, sql);
			pstmt.setInt(1, Integer.parseInt(userId));
			rs =  pstmt.executeQuery();
			while(rs.next()){
//				sb.append(String.valueOf(rs.getInt("userId"))).append('-').
			    sb.append(String.valueOf(rs.getInt("consumeId"))).append('-').
				   append(rs.getString("foodName")).append('-').
				   append(String.valueOf(rs.getInt("foodPrice"))).append('-').
				   append(String.valueOf(rs.getInt("foodNum"))).append('-').
				   append(String.valueOf(rs.getInt("foodTotprice"))).append('-').
				   append(String.valueOf(rs.getInt("foodMark")));
			    
				sb.append("/");  
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeConnection(conn);
			DBUtil.closeStatement(pstmt);
		}
		return sb.toString();
	}
	public void update(String foodMark,String consumeId){
		String sql = "update  t_consumeinfo set foodMark=?  where consumeId =?";
		StringBuffer sb = new StringBuffer("");
		try {
			conn = DBUtil.getConnection();
			pstmt = DBUtil.getPreparedStatement(conn, sql);
			pstmt.setInt(1, Integer.parseInt(foodMark));
			pstmt.setInt(2, Integer.parseInt(consumeId));
			pstmt.executeUpdate();

			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeConnection(conn);
			DBUtil.closeStatement(pstmt);
		}
//		return sb.toString();
	}
	
	public void deleteRecord(String consumeId){
		
		String sql = "delete from t_consumeinfo where consumeId=?";
		try {
			
			conn = DBUtil.getConnection();
			pstmt = DBUtil.getPreparedStatement(conn, sql);
			pstmt.setInt(1, Integer.parseInt(consumeId));
			pstmt.execute();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeConnection(conn);
			DBUtil.closeStatement(pstmt);
		}
	}
}
