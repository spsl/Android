package xxe.restrant.showfood.domain;
/**
 * 
 * @author ASUS
 * ʳ����Ϣ��װ��
 *
 */
public class Food {
	public int id;//ʳ��id
	public String name;//ʳ������
	public String imgName;//ʳ��ͼƬ
	public int price;//�۸�
	public String describe;//����
	public String foodditals;//����
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
