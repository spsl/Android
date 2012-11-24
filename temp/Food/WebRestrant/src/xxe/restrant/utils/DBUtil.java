package xxe.restrant.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author ASUS
 * JDBC访问数据库封装类
 */

public class DBUtil {

	private static final String DBURL = "jdbc:mysql:///restrant";
	private static final String DBUSERNAME = "root";
	private static final String DBPASSWORD = "root";

	//jdbc:mysql://<hostname>[<:3306>]/<dbname>  com.mysql.jdbc.Driver
	/**
	 * 加载驱动
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
     * 数据库连接，可以创建语句对象用于向数据库发送sql语句
     */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			/**
			 * 驱动程序管理器，负责注册数据库驱动程序，并创建与数据库的连接
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
	 * @return Statement 语句对象，语句对象执行SQL
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
	 * @return PreparedStatement 预编译语句对象 性能较Statement好，解决SQL注入问题
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
     * 关闭连接
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
	 * 关闭连接状态
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

