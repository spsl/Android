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
 * ������Դ��
 * ���������˵�ʳ����Դ���ص��ֻ��ͻ���
 *
 */
public class DownloadInfo {

	//����url
	private static  String url = "http://"+FileService.read()+":8080/WebRestrant";
	//����һ��map���󣬴��ͼƬ
	public static Map<Integer, Bitmap> map = new HashMap<Integer, Bitmap>();
	//����һ��lsit���󣬻�ȡ���е�ʳ��
	public static List<Map<String, Object>> getAllFood(){
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String lineData[] = getLineData();
		//�����������ѭ����ÿ�����ݵĸ�������ֵ��Ȼ�����map�����У����map�������data������
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
	//��ȡʳ����Ϣ��������װ����
	private static Food getFoodData(String string) {
		Food food = new Food();
		String imgName="";
		//��@���зָ��һ��ʳ�����Ϣ����������
		String[] data = string.split("@");
		food.id = Integer.parseInt(data[0]);
		food.name = data[1];
		imgName = data[2];
		food.price = Integer.parseInt(data[3]);
		//���ȴ���4˵��ʳ����������ͼƬ��������Ϣ
		if(data.length>4)
			food.describe = data[4];
		food.foodditals = data[5];
		food.img = downloadImage(imgName);//ͨ������ͼƬ�����֣����ͼƬ
		map.put(food.id, food.img);
		
		return food;
	}
    /**����ͼƬ����
     * ʳ��ͼƬ��Դ����
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
             //����BitmapFactory��ʵ�ֻ�ȡͼƬ
			img = BitmapFactory.decodeStream(in);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}
    //����������Դ
	private static String downloadData(){
		String rs="";
		//����HttpPost����
		HttpPost req = new HttpPost(url+"/ServletShow");
		  
		HttpResponse res = null;
		
		try {
			//ͨ��DefaultHttpClient����ִ���������
			res = new DefaultHttpClient().execute(req);
			//�������״̬����Ϊ200����ʾ��������
			  if (res.getStatusLine().getStatusCode()==200){
				  //����Ӧ��������string������
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
	//���ÿ�е����ݣ�����������
	private static String[] getLineData(){
		String string = downloadData();
	//�Է��ص��ַ�����#���зָ��ÿ������
		int index = string.lastIndexOf("#");
		Log.v(string, "index:"+index);
		String str = string.substring(0, index);
		String[] data = str.split("#");
		return data;
	}

	
}
