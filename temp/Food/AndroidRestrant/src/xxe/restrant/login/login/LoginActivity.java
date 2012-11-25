package xxe.restrant.login.login;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import xxe.restrant.R;
import xxe.restrant.login.Logining;
import xxe.restrant.show.ActivityShowFood;
import xxe.restrant.show.LineAdapter;
import xxe.restrant.utils.FileService;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author  /** �û���¼����
 * 
 * 
 * 
 */
public class LoginActivity extends Activity {
    //���ù��߰�����Ķ�д�ļ���  ��ʵ���������ڶ����ļ��е�IP
	private FileService fileService = new FileService(
			this);
	private static final int LOGIN_DIALOG = 1;//��¼�Ի���
	private static final int DIALOG2 = 2;//���ؽ��ȿ�
	private static final int DIALOG3 = 3; // �޸ķ�����IP�Ի���
	private static final int DIALOG4 = 4;
	private EditText etName, etPsw; //�û���������
	private String editName;
	private String editPwd;
	
	EditText u_nameEditText ;//ע�������û���
	EditText pwd1 ;//����1
	EditText pwd2 ;//����2
	
	String registerName ;
	String registerPwd1;
	String registerPwd2 ;
	
	private String ip; //�û���Ҫ���ӷ�������IP
	private EditText hostip; 
	private String resultData;

	/**
	 * onCreate����
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
         
		
		showDialog(LOGIN_DIALOG); //��ʾ�Ի���
	
	 }
	
    /**
     * ������¼�Ի���
     * @param context
     * @return
     */
	private Dialog createLoginDialog(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.login1, null); //�����¼xml�����ļ�
		etName = (EditText) view.findViewById(R.id.et_userName); //�û���
		etPsw = (EditText) view.findViewById(R.id.et_password); //����
		
		AlertDialog.Builder bulder = new AlertDialog.Builder(context);
		bulder.setView(view);
		bulder.setTitle("�û���¼");//���ñ���
			
		bulder.setNeutralButton("����IP����ע��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDialog(DIALOG3);
			}
		});	
		//ȷ����ť
		bulder.setPositiveButton("��¼", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				editName = etName.getText().toString().trim();
				editPwd = etPsw.getText().toString().trim();
				if(editName.equals("")){//���û���Ϊ��ʱ
					Toast.makeText(LoginActivity.this, "�������û���", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(
							LoginActivity.this,
							LoginActivity.class);
					startActivity(intent);
				
				}
				if(editPwd.equals("")){//������Ϊ��ʱ
					Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(
							LoginActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				else {
					
					
					//showDialog(DIALOG2);

					//�������Ӻ�̨��¼�ӿڲ����ط���˵���Ϣ
					String str=	callLoginPost("http://"+fileService.read()+":8080/WebRestrant/LoginServlet?action=login");
					//Toast.makeText(LoginActivity.this, ":"+str, Toast.LENGTH_SHORT).show();
					if(str.equals("0")){
						removeDialog(LOGIN_DIALOG);
						showDialog("��������ʧ��, ���������Ƿ���Ի�IP�Ƿ���ȷ!");
					}else{
					if(!str.equals("")&&!str.equals("0")){ 
							Intent intent = new Intent(LoginActivity.this, Logining.class);
							String[] temp = str.split("#");
							
							if(ActivityShowFood.map!=null){
								for(int i=0; i<LineAdapter.data.size(); i++){
									ActivityShowFood.mapNum.put((Integer)LineAdapter.data.get(i).get("id"), 1);
									ActivityShowFood.map.put((Integer)LineAdapter.data.get(i).get("id"), false);
								}
							}
							Bundle bundle =new Bundle();
							bundle.putString("userName", temp[0]);
							bundle.putString("userId", temp[1]);
							
							intent.putExtra("UserLogin", bundle);
							
							
							startActivity(intent);
							//myDialog.dismiss();
						}else{
							Toast.makeText(LoginActivity.this, "�û�����������������ԣ�", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(
									LoginActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					}
				}
			}	
			
		});
		//ȡ����ť���˳���¼����
		bulder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setTitle("Exit");
				//LoginActivity.this.finish();
				finish();
			}
		});
		
		
		return bulder.create();
	}
    /**
     * �����Ի���
     */
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		switch (id) {
		case LOGIN_DIALOG:

			return createLoginDialog(this);

		case DIALOG2:
			return building2(this);
		
	   case DIALOG3:
		return building3(this);
		
	   case DIALOG4:
		   return building4(this);
		}
		return null;
		
	}
	
	private Dialog building4(LoginActivity loginActivity) {
		View view = LayoutInflater.from(loginActivity).inflate(R.layout.login2, null); //�����¼xml�����ļ�
		u_nameEditText = (EditText) view.findViewById(R.id.r_userName); //�û���
		pwd1 = (EditText) view.findViewById(R.id.r_password); //����
		pwd2 = (EditText) view.findViewById(R.id.rr_password); //����
		
		
        
		AlertDialog.Builder bulder = new AlertDialog.Builder(loginActivity);
		bulder.setView(view);
		bulder.setTitle("�û�ע��");//���ñ���
		//ȷ����ť
		bulder.setPositiveButton("ע��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			
				registerName = u_nameEditText.getText().toString();
		        registerPwd1 = pwd1.getText().toString();
		        registerPwd2 = pwd2.getText().toString();
				if(registerName.equals("")){//���û���Ϊ��ʱ
					removeDialog(DIALOG4);// �ո�+��/������ݼ�������صĺ���
					showDialog(DIALOG4);
					Toast.makeText(LoginActivity.this, "�������û���", Toast.LENGTH_LONG).show();
					
				}
				if(registerPwd1.equals("")||registerPwd2.equals("")){//������Ϊ��ʱ
					removeDialog(DIALOG4);// �ո�+��/������ݼ�������صĺ���
					showDialog(DIALOG4);
					Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_LONG).show();
					
				}
				else {
					if(registerPwd1.equals(registerPwd2)){
					String str = callRegister("http://"+fileService.read()+":8080/WebRestrant/LoginServlet?action=register");
					Log.e("-----------","----------"+str);
					if(!"".equals(str)){
						Toast.makeText(LoginActivity.this, "��ϲ�㣬ע��ɹ���", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(
								LoginActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
					else{
						removeDialog(DIALOG4);// �ո�+��/������ݼ�������صĺ���
						showDialog(DIALOG4);
						Toast.makeText(LoginActivity.this, "ע��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
						return ;
					}}else{
						removeDialog(DIALOG4);// �ո�+��/������ݼ�������صĺ���
						showDialog(DIALOG4);
						Toast.makeText(LoginActivity.this, "������������벻һ�£������ԣ�", Toast.LENGTH_LONG).show();
					}
				}
			}

			
		});
		//ȡ����ť���˳�ע�����
		bulder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setTitle("Exit");
				Intent intent = new Intent(
						LoginActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});
		
		
		return bulder.create();
		
	}

	private Dialog building2(Context ctx) {
		ProgressDialog prodialog = new ProgressDialog(ctx);
		prodialog.setTitle("Loading...");
		prodialog.setMessage("Connecting to the server, please wait...");
		
	    return prodialog;	
		
		
	}
	
	private Dialog building3(Context ctx) {

		View view = LayoutInflater.from(this).inflate(R.layout.setting, null); // �����ͼ

		hostip = (EditText) view.findViewById(R.id.ip);// ����û���ʵ��
        hostip.setText(fileService.read());
		Builder builder = new Builder(ctx);
		builder.setView(view);

		builder.setTitle("IP"); // ���öԻ������

		builder.setNeutralButton("ע��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDialog(DIALOG4);
			}
		});
		builder.setPositiveButton("ȷ��",  //ȷ������IP��ť
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						if((hostip.getText().toString()).equals("")){
							Toast
							.makeText(
									LoginActivity.this,
									"IP ����Ϊ�գ�������IP",
									Toast.LENGTH_LONG).show();
							removeDialog(DIALOG2);
							
					showDialog(DIALOG2);
						}else{
                        //��IPд���ļ�
						fileService.write(hostip.getText().toString());
				       //
						Toast
								.makeText(
										LoginActivity.this,
										"���¼",
										Toast.LENGTH_LONG).show();
						Intent intent = new Intent(
								LoginActivity.this,
								LoginActivity.class);
						startActivity(intent);
						
					}
					}
				});
		
		//ȡ������ip��ť
		builder.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//DIALOG2;
						showDialog(LOGIN_DIALOG);
					}
				});

		return builder.create();

	}
	 /* ��ʾDialog��method */
    private void showDialog(String mess)
    {
      new AlertDialog.Builder(LoginActivity.this).setTitle("Message")
       .setMessage(mess)
       .setNegativeButton("OK",new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialog, int which)
         {   
        	 showDialog(LOGIN_DIALOG);
         }
       })
       .show();
    }
    private String callLoginPost(String url) {
    	 String rs = "";
  	   try {
  		   List<NameValuePair> param = new ArrayList<NameValuePair>();
  		   param.add(new BasicNameValuePair("username",editName));
  		   param.add(new BasicNameValuePair("pwd",editPwd));   
  		   HttpClient client = new DefaultHttpClient();	
 	 	   HttpParams httpParams = client.getParams();	
 		//���ó�ʱʱ��
 		   HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
  		   HttpPost req = new HttpPost(url);
  		   req.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
  		   HttpResponse res = new DefaultHttpClient().execute(req);
  		   
  		   if(res.getStatusLine().getStatusCode()==200){
  			   String temp = EntityUtils.toString(res.getEntity());
  			   if(temp.length()>0){
  				   rs = temp.substring(0, temp.length()-2);
  				   //Toast.makeText(this, ""+rs, Toast.LENGTH_SHORT).show();
  			   }
  		   }
  		
  	   } catch (Exception e) {
  		   e.printStackTrace();
  		 rs = "0";
  	}
  	   return rs;
	}
    
    
    protected String callRegister(String url){
		String rs = "";
		try {
			HttpPost req = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("registername",registerName ));
			param.add(new BasicNameValuePair("registerpwd",registerPwd2 ));
			
			req.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
			
			HttpResponse res = new DefaultHttpClient().execute(req);
			if(res.getStatusLine().getStatusCode()==200){
				String temp = EntityUtils.toString(res.getEntity());
				Log.e("---------------", "temp:"+temp);
				return temp;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
}