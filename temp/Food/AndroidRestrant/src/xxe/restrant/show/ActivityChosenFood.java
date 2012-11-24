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
 * ѡ��ʳ��
 *
 */
public class ActivityChosenFood extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ϸ������л��list���ݣ���װ��ʳ���������Ϣ
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		ArrayList<String> list = data.getStringArrayList("list");
		//����һ��������������list���������������� 
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		ListView lv = new ListView(this);
		//��ʳ�����Ϣ��ListView��ʾ����
		lv.setAdapter(adapter);
		
		setContentView(lv);
	}
}
