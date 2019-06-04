package utils;

import json.JSONArray;

public class JSONParser {
	/**
	 * 将json数组进行json编码得到json字符串
	 * @param data
	 * @return
	 */
	public static <T> String json_encode(JSONArray array) {
		String json = "";
		
		if(array.isKeyValue()) {
			json += "{";
			// 这是键值对的json数组（关联数组）
			for(int i=0;i<array.size();i++) {
				String key = array.getKeyByIndex(i);
				Object data = array.getDataByIndex(i);
				String data_to_json = "";
				
				if(data instanceof JSONArray) {
					// 如果data仍然是一个JSON数组对象
					data_to_json = JSONParser.json_encode((JSONArray) data);
				} else if(data instanceof Integer || data instanceof Double) {
					// 如果data是数组
					data_to_json = data + "";
				} else if(data instanceof String) {
					// 如果data是字符串
					data_to_json = "\"" + data + "\"";
				} else if(data instanceof Integer[] || data instanceof Double[] || data instanceof String[]) {
					data_to_json = JSONParser.json_encode(JSONArray.arrayToJSONArray((Object[]) data));
				} else if(data instanceof JSONArray[]) {
					JSONArray[] arr = (JSONArray[])data;
					for(int j=0;j<arr.length;j++) {
						String temp = JSONParser.json_encode(arr[j]);
						data_to_json += temp + ",";
					}
					data_to_json = data_to_json.substring(0, data_to_json.length()-1);
				}
				json += "\"" + key + "\":" + data_to_json + ",";
			}
			json = json.substring(0, json.length()-1) + "}";
		} else {
			// 这是普通索引数组
			json = "[";
			for(int i=0;i<array.size();i++) {
				Object data = array.getDataByIndex(i);
				String data_to_json = "";
				
				if(data instanceof JSONArray) {
					// 如果data仍然是一个JSON数组对象
					data_to_json = JSONParser.json_encode((JSONArray) data);
				} else if(data instanceof Integer || data instanceof Double) {
					// 如果data是整数
					data_to_json = data + "";
				} else if(data instanceof String) {
					// 如果data是字符串
					data_to_json = "\"" + data + "\"";
				} else if(data instanceof Integer[] || data instanceof Double[] || data instanceof String[]) {
					// data是数组
					data_to_json = JSONParser.json_encode(JSONArray.arrayToJSONArray((Object[]) data));
				} else if(data instanceof JSONArray[]) {
					JSONArray[] arr = (JSONArray[])data;
					for(int j=0;j<arr.length;j++) {
						String temp = JSONParser.json_encode(arr[j]);
						data_to_json += temp + ",";
					}
					data_to_json = data_to_json.substring(0, data_to_json.length()-1);
				}
				json += (data_to_json + ",");
			}
			json = json.substring(0, json.length()-1) + "]";
		}
		
		return json;
	}
	
	/**
	 * 将json字符串解码为json数组
	 * @param json
	 * @return
	 */
	public static JSONArray json_decode(String json) {
		return null;
	}
}
