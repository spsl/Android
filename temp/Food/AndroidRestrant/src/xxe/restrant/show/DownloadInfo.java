package xxe.restrant.show;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import xxe.restrant.show.domain.Food;
import xxe.restrant.utils.FileService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * 
 * @author ASUS
 * 下载资源类
 * 将服务器端的食物资源下载到手机客户端
 *
 */
public class DownloadInfo {

	//定义url
	private static  String url = "http://"+FileService.read()+":8080/WebRestrant";
	//定义一个map对象，存放图片
	public static Map<Integer, Bitmap> map = new HashMap<Integer, Bitmap>();
	//定义一个lsit对象，获取所有的食物
	public static List<Map<String, Object>> getAllFood(){
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String lineData[] = getLineData();
		//根据数组对象，循环出每条数据的各个属性值，然后放入map对象中，最后将map对象存入data数组中
		for(int i=0; i<lineData.length; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			Food food = getFoodData(lineData[i]);
			map.put("id", food.id);
			map.put("name", food.name);
			map.put("price", food.price);
			map.put("describe", food.describe);
			map.put("foodditals", food.foodditals);
			map.put("img", food.img);
			
			data.add(map);
		}
		return data;
	}
	//获取食物信息将其放入封装类中
	private static Food getFoodData(String string) {
		Food food = new Food();
		String imgName="";
		//以@进行分割，将一条食物的信息放入数组中
		String[] data = string.split("@");
		food.id = Integer.parseInt(data[0]);
		food.name = data[1];
		imgName = data[2];
		food.price = Integer.parseInt(data[3]);
		//长度大于4说明食物有描述，图片，详情信息
		if(data.length>4)
			food.describe = data[4];
		food.foodditals = data[5];
		food.img = downloadImage(imgName);//通过传入图片的名字，获得图片
		map.put(food.id, food.img);
		
		return food;
	}
    /**传入图片名字
     * 食物图片资源下载
     * @param imgName
     * @return
     */
	private static Bitmap downloadImage(String imgName) {
		URL imageurl;
		HttpURLConnection conn;
		Bitmap img = null;
		
		try {
			imageurl = new URL(url+"/images/"+imgName);
			conn = (HttpURLConnection)imageurl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream in = conn.getInputStream();
             //采用BitmapFactory类实现获取图片
			img = BitmapFactory.decodeStream(in);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}
    //下载数据资源
	private static String downloadData(){
		String rs="";
		//采用HttpPost对象
		HttpPost req = new HttpPost(url+"/ServletShow");
		  
		HttpResponse res = null;
		
		try {
			//通过DefaultHttpClient对象执行请求语句
			res = new DefaultHttpClient().execute(req);
			//如果返回状态代码为200，表示正常返回
			  if (res.getStatusLine().getStatusCode()==200){
				  //将响应结果存放在string对象中
				  String temp = EntityUtils.toString(res.getEntity());
				  if (temp.length()>0){
					 rs = temp;
				  }else
				     rs = "error response data length";	
			  }else
				  rs = "error response code:"+res.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
	//获得每行的数据，放入数组中
	private static String[] getLineData(){
		String string = downloadData();
	//对返回的字符串以#进行分割出每条数据
		int index = string.lastIndexOf("#");
		Log.v(string, "index:"+index);
		String str = string.substring(0, index);
		String[] data = str.split("#");
		return data;
	}

	
}
