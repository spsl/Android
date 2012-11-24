package xxe.restrant.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxe.restrant.utils.DBUtil;

import com.mysql.jdbc.Statement;

/**
 * 
 * @author ASUS ��������û���¼�Լ�ע�Ṧ�ܵ�ʵ��
 */

public class LoginServlet extends HttpServlet {
	// ���ݿ�����
	Connection conn;
	PreparedStatement pstmt;
	// �����
	ResultSet rs;
	UserInfo user;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String action = request.getParameter("action");
		PrintWriter out = response.getWriter();
		/**
		 * ����������Ϊloginʱ�� ���е�¼ �Ȼ�ȡ�û��������룬 ͨ���ж��Ƿ���ڷ�����ѯ���ݿ� ��������ͨ����ӡ�������
		 */
		if (action.equals("login")) {
			String username = request.getParameter("username");
			String pwd = request.getParameter("pwd");
			System.out.println("--------------client send patam data:"
					+ username + "  " + pwd);
			String name = isExists(username, pwd);
			out.println(name);
		} else if (action.equals("register")) {// ���Ϊע��
			String registername = request.getParameter("registername");
			String registerpwd = request.getParameter("registerpwd");

			user = new UserInfo();
			user.setUserName(registername);
			user.setUserPwd(registerpwd); //			
			// PrintWriter out = response.getWriter();
			String userId = addUser(user);
			System.out.println(userId);
			out.print(userId);
		}
		out.flush();
		out.close();
	}

	public String isExists(String name, String pwd) {
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from user where userName=? and userPwd=?";
			pstmt = DBUtil.getPreparedStatement(conn, sql);
			pstmt.setString(1, name);
			pstmt.setString(2, pwd);

			rs = pstmt.executeQuery();
			/**
			 * �������������Ϊ�յĻ������û�����id����
			 */
			if (rs.next()) {
				String count = rs.getString("userName");
				String userId = String.valueOf(rs.getInt("userId"));

				System.out.println(count);
				return count + "#" + userId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeStatement(pstmt);
			DBUtil.closeConnection(conn);
		}
		return "";
	}

	// ���û�ע�����Ϣ��ӵ�user���ݿ��У�����id��
	public String addUser(UserInfo user) {
		String sql = "insert into user values(null,?,?)";

		try {
			conn = DBUtil.getConnection();
			pstmt = DBUtil.getPreparedStatement(conn, sql);

			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getUserPwd());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				// System.out.println("============="+rs.getInt(1));
				return String.valueOf(rs.getInt(1));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.closeStatement(pstmt);
			DBUtil.closeConnection(conn);

		}

		return "";
	}

}
