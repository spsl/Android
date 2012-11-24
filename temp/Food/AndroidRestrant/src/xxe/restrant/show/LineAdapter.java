package xxe.restrant.show;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xxe.restrant.R;
import xxe.restrant.details.DetailsActivity;
import xxe.restrant.login.LoginView;
import xxe.restrant.show.domain.Food;
 
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class LineAdapter extends BaseAdapter {
	
	private Context mContext;
	public static List<Map<String, Object>> data = LoginView.data;
	private static List<Map<String, Object>> lessData, moreData;
	private static List<Map<String, Object>> tempData;
	private static String option = "";
	private int num = -1;
	
	
	static{
		//如果显示食物的map对象为空
		if(ActivityShowFood.map == null){
			//进行初始化map对象，一个用于判断是否选择，一个存放对应的食物数量
			ActivityShowFood.map = new HashMap<Integer, Boolean>();
			ActivityShowFood.mapNum = new HashMap<Integer, Integer>();
			for(int i=0; i<data.size(); i++){
				//往map添加值
				ActivityShowFood.mapNum.put((Integer)data.get(i).get("id"), 1);
				ActivityShowFood.map.put((Integer)data.get(i).get("id"), false);
			}
		}
	}
//	private int[] ids = {R.id.ivimg, R.id.tvno, R.id.tvname, R.id.tvprice
//			,R.id.tvdescribe};
	private MyHolder holder;
	private int showId;
	
	public LineAdapter(Context context, int id){
		mContext = context;
		showId = id;//显示食物对应的序号
	}
	
	public static String getOption() {
		return option;
	}
   //设置查询到的菜单
	public static void setOption(String option) {
		LineAdapter.option = option;
	}
    //获取选择菜单的data长度
	public int getCount() {
		if(showId == 0)
			tempData = data;
		else{
			if(lessData==null){
				lessData = new ArrayList<Map<String,Object>>();//菜单价格少的时候使用
				moreData = new ArrayList<Map<String,Object>>();//菜单价格多的时候使用
				for(int i=0; i<data.size(); i++){ //循环数据长度
					if((Integer)data.get(i).get("price") <= 5)//如果价格小于或等于5就把对应的食物放入少的list对象中
						lessData.add(data.get(i));
					else
						moreData.add(data.get(i));//如果价格大于5就把对应的食物放入多的list对象中
				}
			}
			if(showId==1)
				tempData = lessData;
			else
				tempData = moreData;
		}
		//选择到的菜通过菜单名字匹配，取出数据放入list中
		if(!"".equals(option)){
			num++;
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(int i=0; i<tempData.size(); i++){
				String str = tempData.get(i).get("name").toString();
				if(str.contains(option)){
					list.add(tempData.get(i));
					//tempData.remove(i);
					//i--;
				}
			}
			tempData = list;
//			if(tempData.size()==0 && num==0)
//				Toast.makeText(mContext, "抱歉，没有你所要的菜", Toast.LENGTH_SHORT).show();
		}
		return tempData.size();
	}

	public Object getItem(int position) {
		if(tempData!=null && tempData.size()>position){
			return tempData.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
    //界面视图
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.showfood, null);
			holder = new MyHolder();
			
			holder.no = (TextView)convertView.findViewById(R.id.tvno);
			holder.name = (TextView)convertView.findViewById(R.id.tvname);
			holder.price = (TextView)convertView.findViewById(R.id.tvprice);
			holder.describe = (TextView)convertView.findViewById(R.id.tvdescribe);
			holder.img = (ImageView)convertView.findViewById(R.id.ivimg);
			//holder.cbchoose = (CheckBox)convertView.findViewById(R.id.cbchoose);
			holder.ivchoose = (ImageView)convertView.findViewById(R.id.ivchoose);
			holder.detail = (Button)convertView.findViewById(R.id.bdetail);
			
			convertView.setTag(holder);
		}else{
			holder = (MyHolder)convertView.getTag();
		}
		
		holder.no.setText("编号："+tempData.get(position).get("id").toString());
		holder.name.setText(tempData.get(position).get("name").toString());
		holder.price.setText("现价：人民币"+tempData.get(position).get("price").toString()+"元");
		holder.describe.setText(tempData.get(position).get("describe").toString());
		
		holder.img.setImageBitmap((Bitmap)tempData.get(position).get("img"));
		if(ActivityShowFood.map.get(Integer.parseInt(tempData.get(position).get("id").toString()))){
			holder.ivchoose.setBackgroundResource(R.drawable.c5);
			
		}else
			holder.ivchoose.setBackgroundResource(R.drawable.c6);
		//holder.choose.setBackgroundResource(R.drawable.c5);
		holder.ivchoose.setOnClickListener(new MyOnClickListener(position, 0, holder.ivchoose,
				Integer.parseInt(tempData.get(position).get("id").toString())));
		holder.detail.setOnClickListener(new MyOnClickListener(position, 1));
		
		return convertView;
	}
	
	class MyOnClickListener implements OnClickListener{
		int id, no;
		int position;
		ImageView img;
		CheckBox cb;//选择框
		//注册监听是否选择订购
		public MyOnClickListener(int position, int id){
			this.id = id;//传入食物的id
			this.position = position;//编号
		}
		//注册监听是否查看详情
		public MyOnClickListener(int position, int id, ImageView img, int no){
			this.id = id;
			this.position = position;
			this.img = img;
			this.no = no;
		}
		
		public void onClick(View v) {
			if(id==0){ //如果是选择框被选事件
				//boolean flag = img.getDrawable().equals(mContext.getResources().getDrawable(R.drawable.btn_check_on));
				if(ActivityShowFood.map.get(no)){//获取map的编号
					ActivityShowFood.map.put(no, false);//将map中是否选择为没有选上
					img.setBackgroundResource(R.drawable.c6);//设置图片为没有被选上
				}else{
					ActivityShowFood.map.put(no, true); 
					//Toast.makeText(mContext, "abcdef"+position, Toast.LENGTH_SHORT).show();
					img.setBackgroundResource(R.drawable.c5);
				}
			}else if(id==1){//详情图片的点击事件
				//初始化食物类
				Food food = new Food();
				food.id = (Integer)tempData.get(position).get("id");//获取食物的id
				food.name = tempData.get(position).get("name").toString();
				food.price = (Integer)tempData.get(position).get("price");
				food.describe = tempData.get(position).get("describe").toString();
				food.foodditals = tempData.get(position).get("foodditals").toString();
				//food.img = (Bitmap)data.get(position).get("img");
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("food", food);
				Intent intent = new Intent(mContext, DetailsActivity.class);
//				Intent intent = new Intent(mContext, ActivityDetailFood.class);
				if(bundle!=null)
					intent.putExtras(bundle);
				ActivityShowFood.jumpActivity(intent);//带数据进行也没的跳转进入详情界面
				//mContext.startActivity(intent);
				
				//mContext.
			}
		}
		
	}
}
//holder类中显示了界面的各组件
class MyHolder{
	public TextView no;
	public TextView name;
	public ImageView img;
	public TextView price;
	public TextView describe;
	public ImageView ivchoose;
	public Button detail;
}
