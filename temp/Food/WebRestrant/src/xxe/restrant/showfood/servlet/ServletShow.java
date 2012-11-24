package xxe.restrant.showfood.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxe.restrant.showfood.domain.Food;
import xxe.restrant.utils.DBUtil;
/**
 * 
 * @author ASUS
 * ��������������
 *
 */
public class ServletShow extends HttpServlet {
	
	private static final String COLS[] = {"foodId", "foodName", "foodImage", "foodPrice", "foodDes","foodditals"};
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//����������������
		response.setContentType("text/html;charset=UTF-8");

		//request.setCharacterEncoding("GBK");
		Connection conn = DBUtil.getConnection();
		Statement stmt = DBUtil.getStatement(conn);
		String sql = "select * from foodinfo";
		ResultSet rs = null;
		
		PrintWriter out = response.getWriter();
		
		StringBuffer sb = new StringBuffer();
			try {
				rs = stmt.executeQuery(sql);
				//��������������������������food����������
				while(rs.next()){
					Food food = new Food();
					food.id = rs.getInt(rs.findColumn(COLS[0]));
					food.name = rs.getString(rs.findColumn(COLS[1]));
					food.imgName = rs.getString(rs.findColumn(COLS[2]));
					food.price = rs.getInt(rs.findColumn(COLS[3]));
					food.describe = rs.getString(rs.findColumn(COLS[4]));
					food.foodditals = rs.getString(rs.findColumn(COLS[5]));
					
					System.out.println(food.name);
					sb.append(food.toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.println(sb.toString());
			out.flush();
			out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
