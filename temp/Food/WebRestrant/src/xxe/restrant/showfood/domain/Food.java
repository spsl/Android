package xxe.restrant.showfood.domain;
/**
 * 
 * @author ASUS
 * 食物信息封装类
 *
 */
public class Food {
	public int id;//食物id
	public String name;//食物名称
	public String imgName;//食物图片
	public int price;//价格
	public String describe;//描述
	public String foodditals;//详情
	/**
	 * 
	 * @param id
	 * @param name
	 * @param imgName
	 * @param price
	 * @param describe
	 * @param foodditals
	 */
	public Food(int id, String name, String imgName, int price, String describe,String foodditals) {
		super();
		this.id = id;
		this.name = name;
		this.imgName = imgName;
		this.price = price;
		this.describe = describe;
		this.foodditals = foodditals;
	}

	public Food() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id +"@"+ name+ "@" +imgName + 
		"@" + price + "@" +describe + "@"+foodditals+"#";
	}
}
