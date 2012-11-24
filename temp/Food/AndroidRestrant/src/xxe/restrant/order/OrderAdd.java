package xxe.restrant.order;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import xxe.restrant.R;
import xxe.restrant.utils.FileService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OrderAdd extends Activity {
	private FileService fileService = new FileService(
			this);
	///������
	private EditText etOrderName;
	///������ַ
	private EditText etOrderAddr;
	///�����绰
	private EditText etOrderPhone;
	///��������
	private EditText etOrderMessage;
	///�ύ��ť
	private Button btOk;
	
	private ArrayList data;
	
	
	private List<NameValuePair> param;

	private String userName;
	private String userId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderadd);
    
        Bundle bundle =getIntent().getExtras();
     
        
        ///���Ѽ�¼����
         data =bundle.getStringArrayList("ShopCartInfo");
         
         userName = bundle.getString("userName");
 		userId = bundle.getString("userId");
 		setTitle("��ӭ�û�"+userName+"��¼");
 		
        etOrderName =(EditText) findViewById(R.id.etOrderName);
        etOrderAddr = (EditText) findViewById(R.id.etOrderAddr);
        etOrderPhone = (EditText) findViewById(R.id.etOrderPhone);
        etOrderMessage =(EditText) findViewById(R.id.etOrderMessage);
        
        btOk =(Button) findViewById(R.id.btOk);
        
        btOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if("".equals(etOrderName.getText().toString())){
					Toast.makeText(OrderAdd.this, "����������Ϊ��", Toast.LENGTH_SHORT);
				}else if("".equals(etOrderAddr.getText().toString())){
					Toast.makeText(OrderAdd.this, "������ַ����Ϊ��", Toast.LENGTH_SHORT);
				}else if("".equals(etOrderPhone.getText().toString())){
					Toast.makeText(OrderAdd.this, "�����绰����Ϊ��", Toast.LENGTH_SHORT);
				}else {
					
					String action ="add";///�ύ����
					////��ȡ����
					String orderName =etOrderName.getText().toString();
					String orderAddr =etOrderAddr.getText().toString();
					String orderPhone = etOrderPhone.getText().toString();
					String orderMessage =etOrderMessage.getText().toString();
					
					   param = new ArrayList<NameValuePair>();
					   param.add(new BasicNameValuePair("action",action));
					   ///////////////////////
					   param.add(new BasicNameValuePair("userName",userName));
					   ////////////////
					   param.add(new BasicNameValuePair("orderName",orderName));
					   param.add(new BasicNameValuePair("orderAddr",orderAddr));
					   param.add(new BasicNameValuePair("orderPhone", orderPhone));
					   param.add(new BasicNameValuePair("orderMessage",orderMessage));
					   String OrderURL ="http://"+fileService.read()+":8080/WebRestrant/OrderInfoServlet";////����������URL;
					   ///�ύ��������
					   String orderstr= callPost(OrderURL,param);
					   Log.e("=============order", orderstr);
					   
					   
					 ///�������
					   ///��ȡ�����˵���Ŀ
					    int count=  data.size();
					    Log.e("=============shop", ""+count);
					    if(count>0){
					    	//������Ѽ�¼
					    	 action ="add";///�ύ����
					    	 
					    	ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
					        param1.add(new BasicNameValuePair("action",action));
					    	param1.add(new BasicNameValuePair("consumeNum",String.valueOf(count)));
					         int i=0;
						     while(i<count){
						    	  HashMap it=(HashMap) data.get(i);
							    	String foodId =(String) it.get("foodId");
							    	String foodName =(String) it.get("foodName");
							    	String foodPrice =(String) it.get("foodPrice");
							    	String foodNum =(String) it.get("foodNum");
							    	String foodTotalPrice =(String) it.get("foodTotalPrice");
							    	 
								       param1.add(new BasicNameValuePair("userId"+i,userId));
									   param1.add(new BasicNameValuePair("foodId"+i,foodId));
									   param1.add(new BasicNameValuePair("foodName"+i,foodName));
									   param1.add(new BasicNameValuePair("foodPrice"+i, foodPrice.substring(1)));
									   param1.add(new BasicNameValuePair("foodNum"+i,foodNum));
									   param1.add(new BasicNameValuePair("foodTotalPrice"+i,foodTotalPrice.substring(1)));
									   param1.add(new BasicNameValuePair("foodMark"+i,"1"));
											   i++; 
						     }
						     String ShopCartURL ="http://"+fileService.read()+":8080/WebRestrant/ConsumeInfoServlet";////����������URL;
						     String shopstr= callPost(ShopCartURL,param1);
						     Log.e("==================", shopstr);
						        //ShopCartAdd.data = new ArrayList();
							  OrderAdd.this.finish();
					    }
					    
				}
			}
        	
        });
        
        
    }
	
	///�����ȡ��������Դ����
    private String callHttpGet(String url){
    	String rs="";
    	try {
		  HttpGet req = new HttpGet(url);
		  HttpResponse res = new DefaultHttpClient().execute(req);
		  if (res.getStatusLine().getStatusCode()==200){
			  String temp =  EntityUtils.toString(res.getEntity(), HTTP.UTF_8);
			 // String temp = EntityUtils.toString(res.getEntity());
			  Log.e("-----------temp:",temp);
			 temp= temp.replaceAll("\r\n|\n\r|\r|\n", "");
			  rs =temp;
		  }else
			  rs = "error response "+res.getStatusLine().getStatusCode();
		  
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return rs; 
    }
	
////////////////////�ύ�������ݵ�������
	protected String callPost(String url,List<NameValuePair> param2) {
		 String rs="";
	        try {
	        	
	        	
			  HttpPost req = new HttpPost(url);
			  req.setEntity(new UrlEncodedFormEntity(param2,HTTP.UTF_8 ));
			  HttpResponse res = new DefaultHttpClient().execute(req);
			 
			  
			  ///��ȡ����������Ϣ
			  if (res.getStatusLine().getStatusCode()==200){
				  String temp = EntityUtils.toString(res.getEntity());
				 /* if (temp.length()>0){
					 rs = temp.substring(0,5);
				  }else
				     rs = "error response data length";	*/
			  }else
				  rs = "error response code:"+res.getStatusLine().getStatusCode();
			  
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
	}
}