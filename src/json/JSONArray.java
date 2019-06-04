package json;

import java.util.LinkedList;

/**
 * JSON数组
 * json数组指的是一类键值对，其中键位字符串，值可以为基本类型、基本类型数组和json数组
 * 基本类型包括int double String
 * @author fuhao
 */
public class JSONArray {
	private LinkedList<String> keys;
	private LinkedList<Object> datas;
	
	// 是否是键值对的json数组
	private boolean isKeyValue;
	
	private Integer index;
	
	public JSONArray() {
		this(true);
	}
	
	public JSONArray(boolean isKeyValue) {
		this.index = 0;
		this.isKeyValue = isKeyValue;
		this.keys = new LinkedList<>();
		this.datas = new LinkedList<>();
	}
	
	public void set(String key, Object data) {
		this.isKeyValue = true;
		this.keys.add(key);
		this.datas.add(data);
	}
	
	public void set(Object data) {
		this.keys.add(this.index+++"");
		this.datas.add(data);
	}
	
	public Object get(String key) {
		int index = this.keys.indexOf(key);
		return this.datas.get(index);
	}
	
	public Integer size() {
		return this.keys.size();
	}
	
	public void setKeyValue(boolean isKeyValue) {
		this.isKeyValue = isKeyValue;
	}
	
	public boolean isKeyValue() {
		return isKeyValue;
	}
	
	/**
	 * 根据索引获取键
	 * @param index
	 * @return
	 */
	public String getKeyByIndex(int index) {
		if(this.isKeyValue) {
			if(index < this.keys.size()) {
				return this.keys.get(index);
			}
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据索引获取值
	 * @param index
	 * @return
	 */
	public Object getDataByIndex(int index) {
		if(index < this.datas.size()) {
			return this.datas.get(index);
		}
		return null;
	}
	
	/**
	 * 普通数组向json数组转换
	 * @param data
	 * @return
	 */
	public static JSONArray arrayToJSONArray(Object[] data) {
		JSONArray array = new JSONArray(false);
		for(int i=0;i<data.length;i++) {
			array.set(data[i]);
		}
		return array;
	}
	
	/**
	 * 普通数组向json数组转换
	 * @param data
	 * @return
	 */
	public static <T> JSONArray arrayToJSONArray(LinkedList<T> data) {
		JSONArray array = new JSONArray(false);
		for(int i=0;i<data.size();i++) {
			array.set(data.get(i));
		}
		return array;
	}
}
