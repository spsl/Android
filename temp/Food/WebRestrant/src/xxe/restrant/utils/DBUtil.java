package xxe.restrant.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author ASUS
 * JDBC�������ݿ��װ��
 */

public class DBUtil {

	private static final String DBURL = "jdbc:mysql:///restrant";
	private static final String DBUSERNAME = "root";
	private static final String DBPASSWORD = "root";

	//jdbc:mysql://<hostname>[<:3306>]/<dbname>  com.mysql.jdbc.Driver
	/**
	 * ��������
	 */
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    /**
     * 
     * @return conn
     * ���ݿ����ӣ����Դ������������������ݿⷢ��sql���
     */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			/**
			 * �������������������ע�����ݿ��������򣬲����������ݿ������
			 */
			conn = DriverManager.getConnection(DBURL, DBUSERNAME, DBPASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 
	 * @param conn
	 * @return Statement ������������ִ��SQL
	 */
	public static Statement getStatement(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	/**
	 * 
	 * @param conn
	 * @param sql
	 *            insert into t_user values (?,?,?)
	 * @return PreparedStatement Ԥ���������� ���ܽ�Statement�ã����SQLע������
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,
			String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
    /**
     * �ر�����
     * @param conn
     */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * �ر�����״̬
	 * @param stmt
	 */

	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

