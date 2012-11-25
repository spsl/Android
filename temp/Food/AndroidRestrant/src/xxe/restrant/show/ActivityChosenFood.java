package xxe.restrant.show;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 
 * @author ASUS
 * 选择食物
 *
 */
public class ActivityChosenFood extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//从上个界面中获得list数据，封装了食物的所有信息
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		ArrayList<String> list = data.getStringArrayList("list");
		//定义一个简单适配器，将list对象放入该适配器中 
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		ListView lv = new ListView(this);
		//将食物的信息在ListView显示出来
		lv.setAdapter(adapter);
		
		setContentView(lv);
	}
}
