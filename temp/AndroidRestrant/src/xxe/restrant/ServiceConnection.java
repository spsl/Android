package xxe.restrant;


import java.net.HttpURLConnection;
import java.net.URL;

import xxe.restrant.domain.Customer;


/**
 * 
 * @author ASUS
 * 连接服务器公共类
 *
 */
public class ServiceConnection {
	/**
	 * HttpURLConnection类，用户打开连接
	 * @param location
	 * @return
	 */
	public static HttpURLConnection serviceConnetion(String location){
//			Customer customer = new Customer();
		HttpURLConnection  conn = null;
			try {
				URL url = new URL(location);
				
				conn = (HttpURLConnection)url.openConnection();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return conn;
	}
     
//	
//	public static Customer LoginService(String name,String pwd){
//		Customer customer = new Customer();
//		return customer;
//	}
		
}
