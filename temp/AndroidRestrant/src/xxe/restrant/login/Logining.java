package xxe.restrant.login;


import xxe.restrant.show.ActivityShowFood;
import xxe.restrant.show.DownloadInfo;
import xxe.restrant.show.LineAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
/**
 * @author ASUS
 * 用户登录后进入的第一个界面在这个界面中，通过
 * Intent对象获得上个界面传递的用户名和密码，并且为标题
 * 栏提示用户登录中，在此界面中设置一个用户登录动画视图
 *
 */
public class Logining extends Activity {
 
	public static String userName;
	public static String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=this.getIntent();
		Bundle bundle =intent.getBundleExtra("UserLogin");
		userName =bundle.getString("userName");
		userId =bundle.getString("userId");
		  setTitle("欢迎用户"+userName+"登录中...");
		setContentView(new LoginView(this, this));
	
	}
    /**
     * 设置手机的返回键为无效
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//super.onKeyDown(keyCode, event);
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return false;
		}
		return true;
	}
	
	
}
