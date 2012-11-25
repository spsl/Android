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
 * @author  /** 用户登录界面
 * 
 * 
 * 
 */
public class LoginActivity extends Activity {
    //引用工具包下面的读写文件类  并实例化，用于读出文件中的IP
	private FileService fileService = new FileService(
			this);
	private static final int LOGIN_DIALOG = 1;//登录对话框
	private static final int DIALOG2 = 2;//加载进度框
	private static final int DIALOG3 = 3; // 修改服务器IP对话框
	private static final int DIALOG4 = 4;
	private EditText etName, etPsw; //用户名和密码
	private String editName;
	private String editPwd;
	
	EditText u_nameEditText ;//注册界面的用户名
	EditText pwd1 ;//密码1
	EditText pwd2 ;//密码2
	
	String registerName ;
	String registerPwd1;
	String registerPwd2 ;
	
	private String ip; //用户需要连接服务器的IP
	private EditText hostip; 
	private String resultData;

	/**
	 * onCreate方法
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
         
		
		showDialog(LOGIN_DIALOG); //显示对话框
	
	 }
	
    /**
     * 创建登录对话框
     * @param context
     * @return
     */
	private Dialog createLoginDialog(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.login1, null); //引入登录xml界面文件
		etName = (EditText) view.findViewById(R.id.et_userName); //用户名
		etPsw = (EditText) view.findViewById(R.id.et_password); //密码
		
		AlertDialog.Builder bulder = new AlertDialog.Builder(context);
		bulder.setView(view);
		bulder.setTitle("用户登录");//设置标题
			
		bulder.setNeutralButton("设置IP或者注册", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDialog(DIALOG3);
			}
		});	
		//确定按钮
		bulder.setPositiveButton("登录", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				editName = etName.getText().toString().trim();
				editPwd = etPsw.getText().toString().trim();
				if(editName.equals("")){//当用户名为空时
					Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(
							LoginActivity.this,
							LoginActivity.class);
					startActivity(intent);
				
				}
				if(editPwd.equals("")){//当密码为空时
					Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(
							LoginActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				else {
					
					
					//showDialog(DIALOG2);

					//调用连接后台登录接口并返回服务端的信息
					String str=	callLoginPost("http://"+fileService.read()+":8080/WebRestrant/LoginServlet?action=login");
					//Toast.makeText(LoginActivity.this, ":"+str, Toast.LENGTH_SHORT).show();
					if(str.equals("0")){
						removeDialog(LOGIN_DIALOG);
						showDialog("网络连接失败, 请检查网络是否可以或IP是否正确!");
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
							Toast.makeText(LoginActivity.this, "用户名或密码错误请重试！", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(
									LoginActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					}
				}
			}	
			
		});
		//取消按钮，退出登录界面
		bulder.setNegativeButton("取消", new OnClickListener() {

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
     * 创建对话框
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
		View view = LayoutInflater.from(loginActivity).inflate(R.layout.login2, null); //引入登录xml界面文件
		u_nameEditText = (EditText) view.findViewById(R.id.r_userName); //用户名
		pwd1 = (EditText) view.findViewById(R.id.r_password); //密码
		pwd2 = (EditText) view.findViewById(R.id.rr_password); //密码
		
		
        
		AlertDialog.Builder bulder = new AlertDialog.Builder(loginActivity);
		bulder.setView(view);
		bulder.setTitle("用户注册");//设置标题
		//确定按钮
		bulder.setPositiveButton("注册", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			
				registerName = u_nameEditText.getText().toString();
		        registerPwd1 = pwd1.getText().toString();
		        registerPwd2 = pwd2.getText().toString();
				if(registerName.equals("")){//当用户名为空时
					removeDialog(DIALOG4);// 空格+“/”：快捷键出来相关的函数
					showDialog(DIALOG4);
					Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
					
				}
				if(registerPwd1.equals("")||registerPwd2.equals("")){//当密码为空时
					removeDialog(DIALOG4);// 空格+“/”：快捷键出来相关的函数
					showDialog(DIALOG4);
					Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					
				}
				else {
					if(registerPwd1.equals(registerPwd2)){
					String str = callRegister("http://"+fileService.read()+":8080/WebRestrant/LoginServlet?action=register");
					Log.e("-----------","----------"+str);
					if(!"".equals(str)){
						Toast.makeText(LoginActivity.this, "恭喜你，注册成功！", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(
								LoginActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
					else{
						removeDialog(DIALOG4);// 空格+“/”：快捷键出来相关的函数
						showDialog(DIALOG4);
						Toast.makeText(LoginActivity.this, "注册失败，请重试！", Toast.LENGTH_LONG).show();
						return ;
					}}else{
						removeDialog(DIALOG4);// 空格+“/”：快捷键出来相关的函数
						showDialog(DIALOG4);
						Toast.makeText(LoginActivity.this, "两次输入的密码不一致，请重试！", Toast.LENGTH_LONG).show();
					}
				}
			}

			
		});
		//取消按钮，退出注册界面
		bulder.setNegativeButton("取消", new OnClickListener() {

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

		View view = LayoutInflater.from(this).inflate(R.layout.setting, null); // 获得视图

		hostip = (EditText) view.findViewById(R.id.ip);// 获得用户名实例
        hostip.setText(fileService.read());
		Builder builder = new Builder(ctx);
		builder.setView(view);

		builder.setTitle("IP"); // 设置对话框标题

		builder.setNeutralButton("注册", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDialog(DIALOG4);
			}
		});
		builder.setPositiveButton("确定",  //确定设置IP按钮
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						if((hostip.getText().toString()).equals("")){
							Toast
							.makeText(
									LoginActivity.this,
									"IP 不能为空，请输入IP",
									Toast.LENGTH_LONG).show();
							removeDialog(DIALOG2);
							
					showDialog(DIALOG2);
						}else{
                        //将IP写入文件
						fileService.write(hostip.getText().toString());
				       //
						Toast
								.makeText(
										LoginActivity.this,
										"请登录",
										Toast.LENGTH_LONG).show();
						Intent intent = new Intent(
								LoginActivity.this,
								LoginActivity.class);
						startActivity(intent);
						
					}
					}
				});
		
		//取消设置ip按钮
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//DIALOG2;
						showDialog(LOGIN_DIALOG);
					}
				});

		return builder.create();

	}
	 /* 显示Dialog的method */
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
 		//设置超时时间
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