package xxe.restrant.details;

import xxe.restrant.R;
import xxe.restrant.show.ActivityShowFood;
import xxe.restrant.show.DownloadInfo;
import xxe.restrant.show.domain.Food;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DetailsActivity extends Activity {
	private TextView tvfdname;//菜品名字
	private TextView tvfdprice;//菜品单价
	private TextView tvfdid;   //菜品编号
	private TextView tvfdcontent; //菜品详细信息
	private TextView tvfdms;     //菜品的简单介绍
	private CheckBox cbbuy;    //订购
	private ImageView ivimag;     //菜品图片
	private ImageView ivLimag;    //菜品大图
	private Intent intent;
	private Food food;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		tvfdname = (TextView) this.findViewById(R.id.cpname);
		tvfdprice = (TextView) this.findViewById(R.id.fdprice);
		tvfdid = (TextView) this.findViewById(R.id.tvfdid);
		tvfdcontent = (TextView) this.findViewById(R.id.tvfdinfo);
		cbbuy =  (CheckBox) this.findViewById(R.id.cbbuy);
		ivimag = (ImageView) this.findViewById(R.id.fdsimg);
		ivLimag = (ImageView) this.findViewById(R.id.fdlimag);
		tvfdms = (TextView) this.findViewById(R.id.ms);
		
		
		intent = getIntent();
		Bundle data = intent.getExtras();
		food = (Food)data.getSerializable("food");
		Bitmap img = DownloadInfo.map.get(food.id);
		
		tvfdid.setText(food.id+"");
		tvfdname.setText(food.name+"");
		tvfdprice.setText(food.price+"");
		tvfdcontent.setText(food.foodditals+"");
		tvfdms.setText(food.describe+"");
		ivimag.setImageBitmap(img);
		ivLimag.setImageBitmap(img);
		if(ActivityShowFood.map.get(food.id)){
			cbbuy.setChecked(true);
		}
		
		cbbuy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ActivityShowFood.map.put(food.id, cbbuy.isChecked());
			}
		});
		
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		super.onKeyDown(keyCode, event);
//		if(KeyEvent.KEYCODE_BACK==keyCode){
//			setResult(2, getIntent());
//			Intent intent = new Intent(this, ActivityShowFood.class);
//			startActivity(intent);
//			finish();
//		}
//		return true;
//	}

	
}
