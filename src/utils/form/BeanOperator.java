package utils.form;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

/**
 * 根据属性名称和属性值，设置对象的属性
 * @author fuhao
 */
public class BeanOperator {
	private Object obj;
	private String fieldName;
	private Object value;
	
	private Object fieldObj;
	private Field field;
	private String name;
	
	/**
	 * 给obj对象设置属性值
	 * @example 假设一个雇员在一个部门，一个部门在一个公司，则属性名称为emp.dept.company.name时
	 * 表示设置雇员所在的公司名称，所以设置对象属性值的关键在于拆分
	 * @param obj 对象
	 * @param fieldName 属性名称(对象.属性.属性...)
	 * @param value 属性值
	 */
	public BeanOperator(Object obj, String fieldName, Object value) {
		this.obj = obj;
		this.fieldName = fieldName;
		this.value = value;
		try {
			// 第一步：通过拆分找到最底层的对象fieldObj和属性field
			this.split();
			
			// 第二步：给最底层的对象的指定属性赋值
			this.setValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拆分属性名称
	 * @throws Exception 
	 */
	private void split() throws Exception {
		String[] res = this.fieldName.split("\\.");		// 根据点进行拆分
		this.fieldObj = this.obj;
		
		// res[0]必然是一个对象名称，则从res[1]开始进行操作
		for(int i=1;i<res.length;i++) {
			if(i==res.length-1) {
				// res[res.length-1]，最后一个必然是属性
				this.field = this.fieldObj.getClass().getDeclaredField(res[i]);
				this.name = res[i];
			} else {
				// 此处应该实例化下一层对象
				// 首先获得getter方法，从getter方法中获取对象
				Method met = this.fieldObj.getClass().getDeclaredMethod("get" + StringUtils.upperCap(res[i]));
				this.fieldObj = met.invoke(this.fieldObj);
			}
		}
	}
	
	private void setValue() throws Exception {
		// 需要根据属性类型进行转型
		Method met = this.fieldObj.getClass().getDeclaredMethod("set" + StringUtils.upperCap(this.name), this.field.getType());
		String type = this.field.getType().getSimpleName();
		if(value instanceof String) {
			// 表示设置的属性是单个值而不是数组
			String val = value.toString();
			switch(type) {
				case "Integer":
					if(val.matches("\\d+")) {
						met.invoke(this.fieldObj, Integer.parseInt(val));
					}
					break;
				case "Double":
					if(val.matches("\\d+\\.\\d+")) {
						met.invoke(this.fieldObj, Double.parseDouble(val));
					}
					break;
				case "String":
					met.invoke(this.fieldObj, val);
					break;
				case "Date":
					// 日期类型只支持yyyy-MM-dd格式
					if(val.matches("\\d{4}-\\d{2}-\\d{2}")) {
						met.invoke(this.fieldObj, new SimpleDateFormat("yyyy-MM-dd").parse(val));
					}
					break;
			}
		} else if(value instanceof String[]) {
			// 数组属性的设置
			this.field.setAccessible(true);
			String[] val = (String[]) this.value;
			switch(type) {
				case "String[]":
					this.field.set(this.fieldObj, val);
					break;
				case "Integer[]":
					Integer[] data = new Integer[val.length];
					for(int i=0;i<data.length;i++) {
						data[i] = Integer.parseInt(val[i]);
					}
					this.field.set(this.fieldObj, data);
					break;
				case "Double[]":
					Double[] dataDouble = new Double[val.length];
					for(int i=0;i<dataDouble.length;i++) {
						dataDouble[i] = Double.parseDouble(val[i]);
					}
					this.field.set(this.fieldObj, dataDouble);
					break;
			}
		} else {
			// 错误的设置
		}
	}
}
