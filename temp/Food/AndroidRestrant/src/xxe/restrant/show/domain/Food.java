package xxe.restrant.show.domain;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Food implements Serializable{
	//定义一个序列号，方便于网络传输
	private static final long serialVersionUID = 9094855592388785602L;
	
	public int id;//食物的id
	public String name;//食物的名称
	public Bitmap img;//食物图片
	public int price;//食物的价格
	public String describe;//食物的描述
	public String foodditals;//食物详情
	
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
