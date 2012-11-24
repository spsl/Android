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
 * �û���¼�����ĵ�һ����������������У�ͨ��
 * Intent�������ϸ����洫�ݵ��û��������룬����Ϊ����
 * ����ʾ�û���¼�У��ڴ˽���������һ���û���¼������ͼ
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
		  setTitle("��ӭ�û�"+userName+"��¼��...");
		setContentView(new LoginView(this, this));
	
	}
    /**
     * �����ֻ��ķ��ؼ�Ϊ��Ч
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
