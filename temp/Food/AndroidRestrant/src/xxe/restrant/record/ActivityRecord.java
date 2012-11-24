package xxe.restrant.record;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import xxe.restrant.ServiceConnection;
import xxe.restrant.utils.FileService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * ��ʷ��¼ģ��
 * @author ASUS
 *
 */
public class ActivityRecord extends Activity {
	private FileService fileService = new FileService(
			this);

	String consumeId;//������id
	EditText foodmark;//����
	private TextView tv;
	private String str ;
	private final String[] key =new String[]{"consumeId","foodName","foodPrice","foodNum","foodTotprice","foodMark"};
	private SimpleAdapter adapter;
	private ListView listView;
	private String userName;
	private String userId;
	
	View dialogitem;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
//		Intent requestIntent = this.getIntent();
		userId = this.getIntent().getStringExtra("userId");
		
		
		tv =(TextView)this.findViewById(R.id.orderlog_id);
		listView = (ListView)this.findViewById(R.id.listId);
		
		show();
			
	}
	///�����˵���
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    menu.add(0,Menu.FIRST,1,"����").setIcon(getResources().getDrawable(R.drawable.ic_menu_pf));
	    menu.add(1,Menu.FIRST+1,2,"ɾ�����Ѽ�¼").setIcon(getResources().getDrawable(R.drawable.ic_menu_delete));
	    menu.findItem(Menu.FIRST);
	    return true;
	}
	//�˵���ѡ���¼�
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch(item.getItemId()){
		  case Menu.FIRST:{
			  //ѡ��һ�����ݽ�������
			  LinearLayout view =(LinearLayout) listView.getSelectedView();
			  if (view!=null){//���ѡ��һ�����ݣ����������ߵ�id
			     TextView tid =(TextView) view.getChildAt(0);
			     consumeId = tid.getText().toString();				     
			     showDialog(1);  //��ʾ���ֶԻ���   
			  }
			  break;
		  }	
		  case Menu.FIRST+1:{
			  //ѡ��һ�����ݽ���ɾ������
			  LinearLayout view =(LinearLayout) listView.getSelectedView();
			  if (view!=null){
				  //���ѡ�У����������ߵ�id
			     TextView tid =(TextView) view.getChildAt(0);
			     String id = tid.getText().toString();
			     try {  //����url
						String location = "http://"+fileService.read()+":8080/WebRestrant/RecordeServlet?action=delete";
						HttpPost req = new HttpPost(location);//	����post�������������
						List<NameValuePair> param = new ArrayList<NameValuePair>();
						param.add(new BasicNameValuePair("consumeId",id));
						req.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
						
						HttpResponse res = new DefaultHttpClient().execute(req);
						if(res.getStatusLine().getStatusCode()==200){
							 str = EntityUtils.toString(res.getEntity());
							 
							 Log.e("=============", str);
							 
						}else str ="error respone";
					} catch (Exception e) {
						e.printStackTrace();
					} 
					show();
//	             Intent intent = new Intent(ActivityRecord.this,ActivityRecord.class);
//	             startActivity(intent);	
					
			  }
		  }
		}
		return true;
	}
	
	//���ڲ�ѯ���е���ʷ��¼���ص��ַ������зָ
	//��map�����װ��һ����¼���ڷ��뵽list������
	public List strSplitToList(String str){
		
		List<HashMap> data = new ArrayList<HashMap>();
		HashMap map = null;
		///str.split("/").length
		
		String[] record = str.split("/");
		Log.e("=============record.length", ""+record.length);
//		Log.e("=============record", ""+record[6]);
		String [] recordItem;

	
		
		for (int i = 0; i < record.length-1; i++) {
			recordItem = record[i].split("-");
			map = new HashMap();
			for (int j = 0; j < recordItem.length; j++) {
				map.put(key[j],recordItem[j]);
			}
			data.add(map);
		}
		return data;
	}
	//�����Ի���
	@Override
	protected Dialog onCreateDialog(int id) {
		 super.onCreateDialog(id);
		 Dialog dialog = null;
		 if(id==1){
			 dialog =builder1(this); 
		 }
		 return dialog;
	}
	private Dialog builder1(Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		
////////���ʺ�̨�������ֽ��и��²���
		final String location = "http://"+FileService.read()+":8080/WebRestrant/RecordeServlet?action=update";
		
		dialogitem = LayoutInflater.from(this).inflate(R.layout.mark, null);
		builder.setView(dialogitem);
		 foodmark = (EditText) dialogitem.findViewById(R.id.etmark);
		 builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String foodMark =foodmark.getText().toString().trim();
				if(!"".equals(foodMark)){
					 try { 
							HttpPost req = new HttpPost(location);
							List<NameValuePair> param = new ArrayList<NameValuePair>();
							param.add(new BasicNameValuePair("consumeId",String.valueOf(consumeId)));
							param.add(new BasicNameValuePair("foodMark",foodMark));
							req.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
							HttpResponse res = new DefaultHttpClient().execute(req);
						} catch (Exception e) {
							e.printStackTrace();
						}
						show();
				}
			}
		});
		 builder.setNegativeButton("ȡ��", null);
		
		return builder.create();
	}
	//��ʾ���е����Ѽ�¼
	public void show(){
		
		 try {
			String location = "http://"+fileService.read()+":8080/WebRestrant/RecordeServlet?action=record";
			HttpPost req = new HttpPost(location);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("userId",userId));
			req.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			
			HttpResponse res = new DefaultHttpClient().execute(req);
			if(res.getStatusLine().getStatusCode()==200){
				 str = EntityUtils.toString(res.getEntity());
				 
				 Log.e("=============", str);
				 
			}else str ="error respone";
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		List data = strSplitToList(str);
		
		adapter = new SimpleAdapter(this, data, R.layout.listitemrecord, key, new int[]{R.id.tvno,R.id.tvname,R.id.tvnum,R.id.tvprice,R.id.tvtotprice,R.id.tvmark});
		
		listView.setAdapter(adapter);
		
	}
	
 
    
}
