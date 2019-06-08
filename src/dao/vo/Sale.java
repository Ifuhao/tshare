package dao.vo;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Sale implements Serializable {
	private int sale_id;		// 主键
	private String id;			// 发布者邮箱
	private String title;		// 商品标题
	private String category;	// 商品分类
	private String description;	// 商品描述
	private int num;			// 商品数量
	private float buy_price;	// 买入价格
	private String buy_way;		// 商品来源
	private int sale_new;		// 是否全新
	private float price;		// 预期价格
	private int bargain;		// 能否议价(0不能/1能)
	private int view;			// 浏览量
	private int delivery;		// 能否配送(0不能/1能)
	private String picture;		// 参考图片
	private Date time;			// 发布时间
	private int is_sell;		// 是否交易成功
	private int is_delete;			// 是否删除
	
	public Sale() {}

	public int getSale_id() {
		return sale_id;
	}

	public void setSale_id(int sale_id) {
		this.sale_id = sale_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public float getBuy_price() {
		return buy_price;
	}

	public void setBuy_price(float buy_price) {
		this.buy_price = buy_price;
	}

	public String getBuy_way() {
		return buy_way;
	}

	public void setBuy_way(String buy_way) {
		this.buy_way = buy_way;
	}

	public int getSale_new() {
		return sale_new;
	}

	public void setSale_new(int sale_new) {
		this.sale_new = sale_new;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getBargain() {
		return bargain;
	}

	public void setBargain(int bargain) {
		this.bargain = bargain;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public int getDelivery() {
		return delivery;
	}

	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getIs_sell() {
		return is_sell;
	}

	public void setIs_sell(int is_sell) {
		this.is_sell = is_sell;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sale_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		if (sale_id != other.sale_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sale [sale_id=" + sale_id + ", id=" + id + ", title=" + title
				+ ", category=" + category + ", description=" + description
				+ ", num=" + num + ", buy_price=" + buy_price + ", buy_way="
				+ buy_way + ", sale_new=" + sale_new + ", price=" + price
				+ ", bargain=" + bargain + ", view=" + view + ", delivery="
				+ delivery + ", picture=" + picture + ", time=" + time
				+ ", is_sell=" + is_sell + ", delete=" + is_delete + "]";
	}
	
	/**
	 * 获取主键的名称
	 * @return
	 */
	public String getKey() {
		return "sale_id";
	}
}
