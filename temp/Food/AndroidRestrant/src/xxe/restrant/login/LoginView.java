package xxe.restrant.login;



import java.util.List;
import java.util.Map;

import xxe.restrant.R;
import xxe.restrant.show.ActivityShowFood;
import xxe.restrant.show.DownloadInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
/**
 * 
 * @author ASUS
 * SurfaceView�����߶���������
 *
 */
public class LoginView extends SurfaceView {
    /**
     * ����������߶������Ų�ͬͼƬ����
     */
	private Bitmap []person=new Bitmap[4];
	private SurfaceHolder holder =null;//�������
	public static List<Map<String, Object>> data;
	///�˵��ƶ�λ��
	private int personX;
	private int personY;
	
	private int totalTime;
	
	private int personSpeed;
	private int personIndex;
	
	private Context mContext;
	private Activity activity;
	public static boolean isLoginOk=false;
	private boolean isLogining;
	public Bitmap background=((BitmapDrawable) getResources().getDrawable(R.drawable.background)).getBitmap();
	
	//�ڹ��캯���н��г�ʼ��
	public LoginView(Context context, Activity activity) {
		super(context);
		mContext =context;
		this.activity = activity;
		holder =this.getHolder();
		holder.addCallback(new SurfaceCallBack());
		isLogining =false;
	}
////����ͼƬ��Դ
	public void InitResource(){
		
		person[0]= BitmapFactory.decodeResource(getResources(), R.drawable.p1);
		person[1] =BitmapFactory.decodeResource(getResources(), R.drawable.p2);
		person[2]= BitmapFactory.decodeResource(getResources(), R.drawable.p3);
		person[3] =BitmapFactory.decodeResource(getResources(), R.drawable.p4);
		
		personX =40;
		personY =100;
		personIndex =0;
		personSpeed =5;
		totalTime =7000;///��ʼ�����߶�ʱ��
		isLoginOk =false;
		
	}
	
  

   public void personLogin(){
	   //����ǰͨ�����������������
	   Canvas canvas =holder.lockCanvas();
	   Paint paint = new Paint();//׼������
	  //���Ʊ���
	   canvas.drawBitmap(background, 0, 0, paint);
	 
//	   canvas.drawColor(Color.WHITE);
	   
	   /////��¼�ɹ���¼��ͷ���߽���ʳ��
	   if(!isLoginOk)
	   canvas.drawBitmap(person[personIndex], personX, personY,null);
	   
	   personIndex++;
	   if(personIndex>=3){
		   personIndex =1;
	   }
	   personX +=personSpeed;
	  //������ɽ�������
	   holder.unlockCanvasAndPost(canvas);   
	   
   }
	
 
   
   class SurfaceThread implements Runnable{

	@Override
	//��run������ʵ�ֵ�¼�������û����������û������������ҳ�����ת
	public void run() {
		if(data!=null){
			isLoginOk=true;
			isLogining =false;
			Intent intentLoginOk = new Intent(mContext, ActivityShowFood.class);
			intentLoginOk.putExtra("userName", Logining.userName);
			intentLoginOk.putExtra("userId", Logining.userId);
			mContext.startActivity(intentLoginOk);
			activity.finish();
		}
		else{
			InitResource();
			personLogin();
			
			//LineAdapter.data = DownloadInfo.getAllFood();	
			
			int pauseTime=250;
			int runningTime =0;
			while(isLogining){		
				try {
					Thread.sleep(pauseTime);
					runningTime +=pauseTime;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			  if(runningTime>=totalTime){
				  isLogining =false; //������¼����
				  isLoginOk =true;
				  personIndex=0;
				  Intent intentLoginOk = new Intent(mContext, ActivityShowFood.class);
					intentLoginOk.putExtra("userName", Logining.userName);
					intentLoginOk.putExtra("userId", Logining.userId);
					mContext.startActivity(intentLoginOk);
					activity.finish();
			  }
			  else if(runningTime>=5000){
				  pauseTime=100;
			  }
			  
			  personLogin();
			  
			}
		}		
	}
	   
   }
   
   //////��������������Դ�߳�
   class DataLogin implements Runnable{

	public void run() {
		if(data==null)
			data = DownloadInfo.getAllFood();
	
	}
   }
   //���������٣��ı��¼����̴��� ��Ҫʵ�� SurfaceHolder.callback  �ӿ�
   class SurfaceCallBack implements Callback{

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isLogining =true;
		new Thread(new SurfaceThread()).start();
		new Thread(new DataLogin()).start();	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isLogining =false;
		isLoginOk =true;
	}
   }
   //�����ֻ��ķ����¼�ΪʧЧ
   public boolean onKeyDown(int keyCode, KeyEvent event) {
		//super.onKeyDown(keyCode, event);
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return false;
		}
		return true;
	}
}
