package xxe.restrant.show.domain;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Food implements Serializable{
	//����һ�����кţ����������紫��
	private static final long serialVersionUID = 9094855592388785602L;
	
	public int id;//ʳ���id
	public String name;//ʳ�������
	public Bitmap img;//ʳ��ͼƬ
	public int price;//ʳ��ļ۸�
	public String describe;//ʳ�������
	public String foodditals;//ʳ������
	
	public Food(int id, String name, Bitmap img, int price, String describe) {
		super();
		this.id = id;
		this.name = name;
		this.img = img;
		this.price = price;
		this.describe = describe;
		this.foodditals = foodditals;
	}

	public Food() {
		super();
	}
}
