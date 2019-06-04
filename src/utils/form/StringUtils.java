package utils.form;

public class StringUtils {
	/**
	 * 将传入字符串首字母大写，其他字母小写
	 * @param str
	 * @return
	 */
	public static String upperCap(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}
	
	/**
	 * 将传入字符串的首字母小写，其他地方不变
	 * @param str
	 * @return
	 */
	public static String lowerCap(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * 判断一个字符串数组中是否存在某个字符串(不区分大小写)
	 * @param needle 目标字符串
	 * @param array 字符串数组
	 * @return
	 */
	public static boolean in_array_ignorecase(String needle, String[] array) {
		for(int i=0;i<array.length;i++) {
			if(needle.equalsIgnoreCase(array[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断一个字符串数组中是否存在某个字符串(区分大小写)
	 * @param needle 目标字符串
	 * @param array 字符串数组
	 * @return
	 */
	public static boolean in_array(String needle, String[] array) {
		for(int i=0;i<array.length;i++) {
			if(needle.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
}
