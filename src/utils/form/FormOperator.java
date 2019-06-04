package utils.form;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据表单填写对象的属性
 * @author fuhao
 */
public class FormOperator<T> {
	
	/**
	 * 根据request提交的表单，填写obj对象的属性
	 * @param obj
	 * @param request
	 */
	public FormOperator(T obj, HttpServletRequest request) {
		Class<?> cls = obj.getClass();
		String clsName = cls.getSimpleName();
		
		Field fields[] = cls.getDeclaredFields();
		for(int i=0;i<fields.length;i++) {
			String fieldName = fields[i].getName();
			String value = request.getParameter(fieldName);
			if(!"null".equals(value)) {
				new BeanOperator(obj, clsName + "." + fieldName, value);
			}
		}
	}
}
