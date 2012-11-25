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
 * SurfaceView人物走动动画界面
 *
 */
public class LoginView extends SurfaceView {
    /**
     * 定义人物的走动的四张不同图片数组
     */
	private Bitmap []person=new Bitmap[4];
	private SurfaceHolder holder =null;//句柄对象
	public static List<Map<String, Object>> data;
	//人的移动位置
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
	
	//在构造函数中进行初始化
	public LoginView(Context context, Activity activity) {
		super(context);
		mContext =context;
		this.activity = activity;
		holder =this.getHolder();
		holder.addCallback(new SurfaceCallBack());
		isLogining =false;
	}
//加载图片资源
	public void InitResource(){
		
		person[0]= BitmapFactory.decodeResource(getResources(), R.drawable.p1);
		person[1] =BitmapFactory.decodeResource(getResources(), R.drawable.p2);
		person[2]= BitmapFactory.decodeResource(getResources(), R.drawable.p3);
		person[3] =BitmapFactory.decodeResource(getResources(), R.drawable.p4);
		
		personX =40;
		personY =100;
		personIndex =0;
		personSpeed =5;
		totalTime =7000;//初始化人的走动时间
		isLoginOk =false;
		
	}
	
  

   public void personLogin(){
	   //绘制前通过句柄对象锁定画布
	   Canvas canvas =holder.lockCanvas();
	   Paint paint = new Paint();//准备画笔
	  //绘制背景
	   canvas.drawBitmap(background, 0, 0, paint);
	 
//	   canvas.drawColor(Color.WHITE);
	   
	   //登录成功登录人头像走进美食店
	   if(!isLoginOk)
	   canvas.drawBitmap(person[personIndex], personX, personY,null);
	   
	   personIndex++;
	   if(personIndex>=3){
		   personIndex =1;
	   }
	   personX +=personSpeed;
	  //绘制完成解锁画布
	   holder.unlockCanvasAndPost(canvas);   
	   
   }
	
 
   
   class SurfaceThread implements Runnable{

	@Override
	//在run方法中实现登录，传入用户名和密码用户名和密码进行页面的跳转
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
				  isLogining =false; //结束登录画面
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
   
   //加载数据网络资源线程
   class DataLogin implements Runnable{

	public void run() {
		if(data==null)
			data = DownloadInfo.getAllFood();
	
	}
   }
   //创建，销毁，改变事件进程处理 需要实现 SurfaceHolder.callback  接口
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
   //设置手机的返回事件为失效
   public boolean onKeyDown(int keyCode, KeyEvent event) {
		//super.onKeyDown(keyCode, event);
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return false;
		}
		return true;
	}
}
