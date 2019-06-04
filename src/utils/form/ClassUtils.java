package utils.form;

public class ClassUtils {
	/**
	 * 将基本类型转换为标准类型，其他类型保持不变
	 * @param cls
	 * @return
	 */
	public static Class<?> getClass(Class<?> cls) {
		String typeName = cls.getSimpleName();
		
		// 八种基本类型：int float double boolean byte char short long
		if(typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("int")) {
			return int.class;
		} else if(typeName.equalsIgnoreCase("float")) {
			return float.class;
		} else if(typeName.equalsIgnoreCase("double")) {
			return double.class;
		} else if(typeName.equalsIgnoreCase("boolean")) {
			return boolean.class;
		} else if(typeName.equalsIgnoreCase("byte")) {
			return byte.class;
		} else if(typeName.equalsIgnoreCase("char")) {
			return char.class;
		} else if(typeName.equalsIgnoreCase("short")) {
			return short.class;
		} else if(typeName.equalsIgnoreCase("long")) {
			return long.class;
		} else {
			return cls;
		}
	}
	
	/**
	 * 判断是否为int类型
	 * @param cls
	 * @return
	 */
	public static boolean isInt(Class<?> cls) {
		String typeName = cls.getSimpleName();
		return typeName.equals("int") || typeName.equals("Integer");
	}
	
	/**
	 * 判断是否为float类型
	 * @param cls
	 * @return
	 */
	public static boolean isFloat(Class<?> cls) {
		String typeName = cls.getSimpleName();
		return typeName.equalsIgnoreCase("float");
	}
	
	/**
	 * 判断是否为String类型
	 * @param cls
	 * @return
	 */
	public static boolean isString(Class<?> cls) {
		String typeName = cls.getSimpleName();
		return typeName.equalsIgnoreCase("String");
	}
}
