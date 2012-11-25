package xxe.restrant.show;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xxe.restrant.R;
import xxe.restrant.login.login.LoginActivity;
import xxe.restrant.order.ShopCartAdd;
import xxe.restrant.record.ActivityRecord;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActivityShowFood extends Activity{
	//private Button bsubmit, bshow;
	private EditText etfind; //查询框
	private Spinner spsort;  //Spinner对象用户下拉选择框
	private int showId = 0; //定义显示食物每个list的id
	private LineAdapter adapter; //定义适配器  
	private ListView list; //用于存放适配器，将适配器中的list对象显示出来
	private int flag = 1; //标志
	public static Map<Integer, Boolean> map; //map对象
	public static Map<Integer, Integer> mapNum;
	private ArrayList<String> chooseFood;//选择的食物的list对象
	private static Activity activity;
	private String userName;
	private String userId;
	private ArrayList foodList;
	
	private static final int DIALOG1 = 1;//对话框用于提示是否退出
	/**
	 * 创建对话框
	 */
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		switch (id) {
		case DIALOG1:
			return building1(this);
		}
		return null;
	}

	
	private Dialog building1(ActivityShowFood activityShowFood) {
		View view = LayoutInflater.from(this).inflate(R.layout.main1, null); // 获得视图
		Builder builder = new Builder(activityShowFood);
		builder.setView(view);

		builder.setTitle("退出提示"); // 设置对话框标题
		builder
				.setMessage("确定退出？");
		builder.setPositiveButton("退出", // 确定设置IP按钮
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//ActivityShowFood.this.finish();
						Intent intent = new Intent(ActivityShowFood.this,LoginActivity.class);
						startActivity(intent);		
					}		
				});
		// 取消设置ip按钮
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeDialog(DIALOG1);
					}
				});

		return builder.create();
	
	}


	//private List<Food> shop;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userId = intent.getStringExtra("userId");
        setTitle("欢迎用户"+userName+"登录");
        activity = this;
        list = (ListView)findViewById(R.id.lvdiaplay);
        //adapter = new LineAdapter(this, showId);
        
        etfind = (EditText)findViewById(R.id.etfind);
        //查询菜谱按钮事件
        etfind.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(etfind.getText() != null){
					//如果输入的不为空将查找用户名除去空格设置到适配器中
					LineAdapter.setOption(etfind.getText().toString().trim());
				}else 
					//如果查找的用户名为空，则显示所有的菜单信息
					LineAdapter.setOption("");
				adapter = new LineAdapter(ActivityShowFood.this, showId);
				list.setAdapter(adapter);
				if(adapter.getCount()==0 && flag==-1){
					Toast.makeText(ActivityShowFood.this, "抱歉，没有你所要的菜", Toast.LENGTH_SHORT).show();
				}
				flag *=-1;
				return false;
			}
		});
        spsort = (Spinner)findViewById(R.id.spsort);
        spsort.setPrompt("选择种类");
        
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
        		,new String[]{"全部","5元及5元以下","5元以上"});
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spsort.setAdapter(adap);
        spsort.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//setTitle(position+"");
				showId = position;
				adapter = new LineAdapter(ActivityShowFood.this, showId);
				list.setAdapter(adapter); 
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

        
        //list.setAdapter(adapter);
    }
    //创建界面的菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(1, Menu.FIRST, 1, "确定").setIcon(getResources().getDrawable(R.drawable.ic_menu_mark));
		menu.add(1, Menu.FIRST+1, 2, "退出").setIcon(getResources().getDrawable(R.drawable.ic_menu_close_clear_cancel));
		menu.add(1, Menu.FIRST+2, 3, "查询历史记录").setIcon(getResources().getDrawable(R.drawable.ic_menu_view));
		
		return true;
	}
    //菜单项的选择事件处理
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case Menu.FIRST:
//			Intent intent1 = new Intent(ActivityShowFood.this,ShopCartAdd.class);
//			startActivity(intent1);
			foodList = new ArrayList();
			//通过循环data的长度，传入i，将data中的数据放入map中，即map中放入的就是一个食物的信息
			for(int i=0; i<LineAdapter.data.size(); i++){
				if(map.get(Integer.parseInt(LineAdapter.data.get(i).get("id").toString()))){
					Map<String, String> map = new HashMap<String, String>();
					map.put("foodId", LineAdapter.data.get(i).get("id")+"");
					map.put("foodName", LineAdapter.data.get(i).get("name")+"");
					map.put("foodPrice", LineAdapter.data.get(i).get("price")+"");
					map.put("foodNum", ""+mapNum.get((Integer)LineAdapter.data.get(i).get("id")));
					
					Log.e("id:"+map.get("foodId"), "name:"+map.get("foodName")+"price:"+map.get("foodPrice"));
					foodList.add(map);
				}
			}
			
			Log.e("==========", "OK");
			Bundle bl =new Bundle();
			bl.putStringArrayList("ShopCart", foodList);
			bl.putString("userName", userName);
			bl.putString("userId", userId);
			Intent intent = new Intent(this,ShopCartAdd.class);
			intent.putExtras(bl);
			startActivity(intent);
			
			break;
		case Menu.FIRST+1:
			ActivityShowFood.this.finish();
			this.finish();	
			//showDialog(DIALOG1);
			break;
		case Menu.FIRST+2:
			
			Intent intent2 = new Intent(ActivityShowFood.this,ActivityRecord.class);
			intent2.putExtra("userName",userName);
			intent2.putExtra("userId", userId);
			startActivity(intent2);
			
			break;
		}
		
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==2){
			adapter = new LineAdapter(ActivityShowFood.this, showId);
			list.setAdapter(adapter); 
		}
	}

	public static void jumpActivity(Intent intent) {
		//activity.finish();
		activity.startActivityForResult(intent, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter = new LineAdapter(ActivityShowFood.this, showId);
		list.setAdapter(adapter); 
		//Toast.makeText(this, "onRE", Toast.LENGTH_SHORT).show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {// 返回键
		case KeyEvent.KEYCODE_BACK:
			showDialog(DIALOG1);
			break;

		}
		return false;
	}


}