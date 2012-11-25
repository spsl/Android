package xxe.restrant.login;
/**
 * 
 * @author 
 *用户信息封装
 */
public class UserInfo {
    /**
     * 用户id
     * 用户名
     * 用户密码
     */
	private int userId;
	private String userName;
	String userPwd;

	public UserInfo() {
		super();
	}
    /**
     * 
     * @param userId
     * @param userName
     * @param userPwd
     */
	public UserInfo(int userId, String userName, String userPwd) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPwd = userPwd;
		
	}
	
    /**
     * 
     * @return userId
     */
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	
}
