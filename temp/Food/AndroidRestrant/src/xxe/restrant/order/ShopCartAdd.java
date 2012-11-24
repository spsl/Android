package xxe.restrant.order;


import java.util.ArrayList;
import java.util.HashMap;

import xxe.restrant.R;
import xxe.restrant.show.ActivityShowFood;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.Editable.Factory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShopCartAdd extends Activity {
	///������ʾ�б���ͼ
    private ListView lv ;
   
	private HashMap selectedItem = new HashMap();
  ////������
	private SimpleAdapter sa;
	
	////���ﳵ������Ϣ
	public static ArrayList data= new ArrayList();
	
    private ArrayList intentReceiveData;
	
	//private ArrayList intentSendData;
	

	private static final int DIALOG=1;
	
	private View shopCartHead;
	
	private View shopCartRoot;
	
	//��ն�����ť
	private Button btClear;
	///�������ﰴť
	private Button btContinue;
	///���ɶ�����ť
	private Button btOrder;
	
	private TextView tvAllPrice;
	
	private int selectIndex =0;
	
	private int foodTotalPrice;
	
	private String userName;
	
	private String userId;
	
	///�޸�������ͼ
	private View dialogitem;

	private TextView tvFoodNum ;
	private TextView tvTotalPrice;
	private TextView tvFoodAllPrice;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		lv = new ListView(this);
		
		
		
	     Bundle bundle =getIntent().getExtras();
		       ///���Ѽ�¼����
		  intentReceiveData  =bundle.getStringArrayList("ShopCart");
		  userName = bundle.getString("userName");
		  userId = bundle.getString("userId");
		  setTitle("��ӭ�û�"+userName+"��¼");
        ///��ȡʳƷ���ݷ�װ��ArrayList    
		  
		data = getFoodData();
		 // data = intentReceiveData;
		///���ﳵ��ʾͷ����ʾ��Ϣ
		 shopCartHead = LayoutInflater.from(this).inflate(
				R.layout.consumehead, null);
		
		///���ﳵ��ʾβ���ܼ���Ϣ
		  shopCartRoot = LayoutInflater.from(this).inflate(
				R.layout.consumeroot, null);
		  
		  
		  tvFoodAllPrice =  (TextView) shopCartRoot.findViewById(R.id.tvRootFoodTotalPrice);
		  tvFoodAllPrice.setText("��"+foodTotalPrice);
		  
       ///����������
        sa =new SimpleAdapter(this,data,R.layout.consumeadd,
        		new String[]{"foodId","foodName","foodPrice","foodNum","foodTotalPrice"},
        		new int[]{R.id.tvFoodId,R.id.tvFoodName,R.id.tvFoodPrice,R.id.tvFoodNum,R.id.tvFoodTotalPrice}
        );
        
        
       ///�б�ѡ�����
        lv.setOnItemSelectedListener(
        		
        new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				selectIndex = arg2;
				tvFoodNum =  (TextView) arg1.findViewById(R.id.tvFoodNum);
				tvTotalPrice = (TextView) arg1.findViewById(R.id.tvFoodTotalPrice);
				////��ѡ�е���
				selectedItem = (HashMap) lv.getItemAtPosition(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
		     
			}
        	
        });
        
        ///////
        lv.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View arg0, boolean arg1) {
				
			}
        	
        });
        
        lv.setBackgroundResource(R.drawable.back);////���ñ�����ɫ
        
     // �����������ͼ��ӵ�ListView��ȥ����ΪListView����ͷ
       
		lv.addHeaderView(shopCartHead,null,false);
		lv.addFooterView(shopCartRoot, null,false);
		

		btClear = (Button) shopCartRoot.findViewById(R.id.btClearShopCart);
		btOrder = (Button) shopCartRoot.findViewById(R.id.btProduceOrder);
		btContinue = (Button) shopCartRoot.findViewById(R.id.btContinueShop);
		
		btClear.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			Log.e("=====================btClear", " is Clicked");
			
			foodTotalPrice =0;
			data =new ArrayList();
	
			  ///����������
	       sa =new SimpleAdapter(ShopCartAdd.this,data,R.layout.consumeadd,
	        		new String[]{"foodId","foodName","foodPrice","foodNum","foodTotalPrice"},
	        		new int[]{R.id.tvFoodId,R.id.tvFoodName,R.id.tvFoodPrice,R.id.tvFoodNum,R.id.tvFoodTotalPrice}
	       
	       );
	       
	       tvFoodAllPrice.setText("��"+foodTotalPrice);
	        lv.setAdapter(sa);
			}
			
		});
		
		btOrder.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				if(data.isEmpty()){
					Toast.makeText(ShopCartAdd.this, "��������Ϊ��", Toast.LENGTH_SHORT).show();
				}else{
				    Intent mintent = new Intent(ShopCartAdd.this,OrderAdd.class);
		  		    Bundle bundle = new Bundle();
		  		    
		  		    bundle.putStringArrayList("ShopCartInfo", data);
		  		    bundle.putString("userName", userName);
		  		    bundle.putString("userId", userId);
		  		    mintent.putExtras(bundle);
		    		startActivity(mintent);
				}
			}
			
		});
		
		///��������
		btContinue.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				
				ShopCartAdd.this.finish();
				
			}
		});
		
        lv.setAdapter(sa);
        setContentView(lv);
	}
	//�Ի��������޸�����
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		switch(id){
		case DIALOG:{return builder(this);}
		}
		return null;
	}

	private Dialog builder(Context ctx){
		dialogitem = LayoutInflater.from(this).inflate(R.layout.dialogitem, null);
		final EditText etNum = ((EditText)dialogitem.findViewById(R.id.etNum));
	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		//��Dialog�����û��Զ���Ľ���
		builder.setView(dialogitem);
		//����Dialog�ı���
		builder.setTitle("�����޸�");
		//����Dialog��ͼ��
		//builder.setIcon(R.drawable.icon);
		//����Dialog����ʾ��Ϣ
		builder.setMessage("�����޸�");
		//����Dialog�ϵ�һ����ť�Ͱ�ť�ĵ����¼�
		builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				
				if(selectedItem!=null){	
					 HashMap it =new HashMap();
						it.put("foodId", selectedItem.get("foodId"));
						it.put("foodName", selectedItem.get("foodName"));
						it.put("foodPrice", selectedItem.get("foodPrice"));
						it.put("foodNum", etNum.getText().toString());
						
						int price =Integer.valueOf(((String) selectedItem.get("foodPrice")).substring(1)).intValue();
						   
						tvFoodNum.setText(etNum.getText().toString());
						
						
						if( etNum.getText().toString()==null|| etNum.getText().toString().equals("")){
							
							tvTotalPrice.setText("��"+0);
						}else{
							
							price=price*Integer.valueOf(etNum.getText().toString()).intValue();
							tvTotalPrice.setText("��"+price);
							
						}
						ActivityShowFood.mapNum.put(Integer.parseInt(selectedItem.get("foodId").toString()), Integer.parseInt(etNum.getText().toString()));
						it.put("foodTotalPrice", tvTotalPrice.getText());
						data.set(selectIndex-1, it);
						Log.e("===foodTotalPrice", ((HashMap)data.get(selectIndex-1)).get("foodTotalPrice").toString().substring(1));
					   sa =new SimpleAdapter(ShopCartAdd.this,data,R.layout.consumeadd,
					        		new String[]{"foodId","foodName","foodPrice","foodNum","foodTotalPrice"},
					        		new int[]{R.id.tvFoodId,R.id.tvFoodName,R.id.tvFoodPrice,R.id.tvFoodNum,R.id.tvFoodTotalPrice}
					       );
					   
				   int i =0;
					   int totalPriceTemp=0;
					   while(i<data.size()){
						   
						 totalPriceTemp+=  Integer.valueOf(((HashMap)data.get(i)).get("foodTotalPrice").toString().substring(1)).intValue();
					   i++;
					   }
						foodTotalPrice =totalPriceTemp;
					   tvFoodAllPrice.setText("��"+foodTotalPrice);
					   lv.setAdapter(sa);
				}
			}
		});
		builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			
			}
		});
		
		return builder.create();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		 super.onCreateOptionsMenu(menu);
		 
		 menu.add(1, Menu.FIRST, 1, "�޸�����").setIcon(getResources().getDrawable(R.drawable.ic_menu_add));
		 
		 return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
	switch(item.getItemId()){
		case Menu.FIRST:
			
			showDialog(DIALOG);
			break;
		}
		return true;
	}

	///��ȡʳƷ����
	private ArrayList getFoodData() {
		
		foodTotalPrice =0;
		ArrayList rs = new ArrayList();
		  int count = intentReceiveData.size();
		  int i=0;
		  HashMap receiveMap;
		  HashMap it;
		try {
		
			while(i<count){
			  
		    receiveMap= (HashMap) intentReceiveData.get(i);
			 it =new HashMap();
			 
			String foodId= (String) receiveMap.get("foodId");
			String foodName = (String) receiveMap.get("foodName"); 
			String foodPrice =(String) receiveMap.get("foodPrice");
			String foodNum =(String) receiveMap.get("foodNum");
			int foodTotal = Integer.valueOf(foodPrice).intValue()*Integer.parseInt(foodNum);
			  it.put("foodId", foodId);
			  it.put("foodName", foodName);/////////
			  it.put("foodPrice",  "��"+foodPrice);/////////"��"+
			  it.put("foodNum",foodNum);/////////
			  it.put("foodTotalPrice","��"+foodTotal);/////////"��"+
			  foodTotalPrice +=foodTotal;
			  rs.add(it);
			  i++;
		  }
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("-----------------row count",""+ rs.size());
		return rs;

	}



	
	
	
}
