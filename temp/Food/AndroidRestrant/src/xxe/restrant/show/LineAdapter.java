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
		//�����ʾʳ���map����Ϊ��
		if(ActivityShowFood.map == null){
			//���г�ʼ��map����һ�������ж��Ƿ�ѡ��һ����Ŷ�Ӧ��ʳ������
			ActivityShowFood.map = new HashMap<Integer, Boolean>();
			ActivityShowFood.mapNum = new HashMap<Integer, Integer>();
			for(int i=0; i<data.size(); i++){
				//��map���ֵ
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
		showId = id;//��ʾʳ���Ӧ�����
	}
	
	public static String getOption() {
		return option;
	}
   //���ò�ѯ���Ĳ˵�
	public static void setOption(String option) {
		LineAdapter.option = option;
	}
    //��ȡѡ��˵���data����
	public int getCount() {
		if(showId == 0)
			tempData = data;
		else{
			if(lessData==null){
				lessData = new ArrayList<Map<String,Object>>();//�˵��۸��ٵ�ʱ��ʹ��
				moreData = new ArrayList<Map<String,Object>>();//�˵��۸���ʱ��ʹ��
				for(int i=0; i<data.size(); i++){ //ѭ�����ݳ���
					if((Integer)data.get(i).get("price") <= 5)//����۸�С�ڻ����5�ͰѶ�Ӧ��ʳ������ٵ�list������
						lessData.add(data.get(i));
					else
						moreData.add(data.get(i));//����۸����5�ͰѶ�Ӧ��ʳ�������list������
				}
			}
			if(showId==1)
				tempData = lessData;
			else
				tempData = moreData;
		}
		//ѡ�񵽵Ĳ�ͨ���˵�����ƥ�䣬ȡ�����ݷ���list��
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
//				Toast.makeText(mContext, "��Ǹ��û������Ҫ�Ĳ�", Toast.LENGTH_SHORT).show();
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
    //������ͼ
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
		
		holder.no.setText("��ţ�"+tempData.get(position).get("id").toString());
		holder.name.setText(tempData.get(position).get("name").toString());
		holder.price.setText("�ּۣ������"+tempData.get(position).get("price").toString()+"Ԫ");
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
		CheckBox cb;//ѡ���
		//ע������Ƿ�ѡ�񶩹�
		public MyOnClickListener(int position, int id){
			this.id = id;//����ʳ���id
			this.position = position;//���
		}
		//ע������Ƿ�鿴����
		public MyOnClickListener(int position, int id, ImageView img, int no){
			this.id = id;
			this.position = position;
			this.img = img;
			this.no = no;
		}
		
		public void onClick(View v) {
			if(id==0){ //�����ѡ���ѡ�¼�
				//boolean flag = img.getDrawable().equals(mContext.getResources().getDrawable(R.drawable.btn_check_on));
				if(ActivityShowFood.map.get(no)){//��ȡmap�ı��
					ActivityShowFood.map.put(no, false);//��map���Ƿ�ѡ��Ϊû��ѡ��
					img.setBackgroundResource(R.drawable.c6);//����ͼƬΪû�б�ѡ��
				}else{
					ActivityShowFood.map.put(no, true); 
					//Toast.makeText(mContext, "abcdef"+position, Toast.LENGTH_SHORT).show();
					img.setBackgroundResource(R.drawable.c5);
				}
			}else if(id==1){//����ͼƬ�ĵ���¼�
				//��ʼ��ʳ����
				Food food = new Food();
				food.id = (Integer)tempData.get(position).get("id");//��ȡʳ���id
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
				ActivityShowFood.jumpActivity(intent);//�����ݽ���Ҳû����ת�����������
				//mContext.startActivity(intent);
				
				//mContext.
			}
		}
		
	}
}
//holder������ʾ�˽���ĸ����
class MyHolder{
	public TextView no;
	public TextView name;
	public ImageView img;
	public TextView price;
	public TextView describe;
	public ImageView ivchoose;
	public Button detail;
}
