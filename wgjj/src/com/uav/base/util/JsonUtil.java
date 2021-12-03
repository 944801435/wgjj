package com.uav.base.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class JsonUtil {
	private static final Logger log = Logger.getLogger(JsonUtil.class);

	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = (PropertyDescriptor[]) null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException localIntrospectionException) {
			log.error("JsonUtil.beanToJson(Object bean)出现异常",localIntrospectionException);
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++)
				try {
					String name = objectToJson(props[i].getName());
					String value = objectToJson(props[i].getReadMethod()
							.invoke(bean, new Object[0]));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception localException) {
					// localException.printStackTrace();
					log.error("JsonUtil.beanToJson(Object bean)出现异常",localException);
				}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((list != null) && (list.size() > 0)) {
			for (Iterator<?> localIterator = list.iterator(); localIterator
					.hasNext();) {
				Object obj = localIterator.next();
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String arrayToJson(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((array != null) && (array.length > 0)) {
			Object[] arrayOfObject = array;
			int j = array.length;
			for (int i = 0; i < j; i++) {
				Object obj = arrayOfObject[i];
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String mapToJson(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if ((map != null) && (map.size() > 0)) {
			for (Iterator<?> localIterator = map.keySet().iterator(); localIterator
					.hasNext();) {
				Object key = localIterator.next();
				json.append(objectToJson(key));
				json.append(":");
				json.append(objectToJson(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String setToJson(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((set != null) && (set.size() > 0)) {
			for (Iterator<?> localIterator = set.iterator(); localIterator
					.hasNext();) {
				Object obj = localIterator.next();
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	private static String numberToJson(Number number) {
		return number.toString();
	}

	private static String booleanToJson(Boolean bool) {
		return bool.toString();
	}

	private static String nullToJson() {
		return "";
	}

	public static String stringToJson(String s) {
		if (s == null) {
			return nullToJson();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if ((ch >= 0) && (ch <= '\037')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}

	public static String objectToJson(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null)
			json.append("\"\"");
		else if ((obj instanceof Number))
			json.append(numberToJson((Number) obj));
		else if ((obj instanceof Boolean))
			json.append(booleanToJson((Boolean) obj));
		else if ((obj instanceof String))
			json.append("\"").append(stringToJson(obj.toString())).append("\"");
		else if ((obj instanceof Object[]))
			json.append(arrayToJson((Object[]) obj));
		else if ((obj instanceof List))
			json.append(listToJson((List<?>) obj));
		else if ((obj instanceof Map))
			json.append(mapToJson((Map<?, ?>) obj));
		else if ((obj instanceof Set))
			json.append(setToJson((Set<?>) obj));
		else if ((obj instanceof Date))
			json.append("\"").append(stringToJson(obj.toString())).append("\"");
		else {
			json.append(beanToJson(obj));
		}
		return json.toString();
	}

	public static Object json2Object(String jsonString, Class<?> pojoCalss) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Object pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}

	public static Map<String, Object> json2Map(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();

		Map<String, Object> valueMap = new HashMap<String, Object>();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	public static Object[] json2ObjectArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	public static List<Object> json2List(String jsonString, Class<?> pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Object pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);
		}
		return list;
	}

	public static String[] json2StringArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}

	public static Long[] json2LongArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = Long.valueOf(jsonArray.getLong(i));
		}
		return longArray;
	}

	public static Integer[] json2IntegerArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = Integer.valueOf(jsonArray.getInt(i));
		}
		return integerArray;
	}

	public static Double[] json2DoubleArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Double[] doubleArray = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			doubleArray[i] = Double.valueOf(jsonArray.getDouble(i));
		}
		return doubleArray;
	}
	
	// 处理JSONObject的key，将key转换位小写
	public static JSONObject transToLowerObject(JSONObject o1) {
        JSONObject o2 = new JSONObject();
         Iterator<?> it = o1.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object object = o1.get(key);
                if(object.getClass().toString().endsWith("String")){
                    o2.accumulate(key.toLowerCase(), object);
                }else if(object.getClass().toString().endsWith("JSONObject")){
                    o2.accumulate(key.toLowerCase(), JsonUtil.transToLowerObject((JSONObject)object));
                }else if(object.getClass().toString().endsWith("JSONArray")){
                    o2.accumulate(key.toLowerCase(), JsonUtil.transToLowerArray(o1.getJSONArray(key)));
                }
            }
            return o2;
    }
	
    public static JSONArray transToLowerArray(JSONArray o1) {
        JSONArray o2 = new JSONArray();
        for (int i = 0; i < o1.size(); i++) {
            Object jArray = o1.getJSONObject(i);
            if(jArray.getClass().toString().endsWith("JSONObject")){
                o2.add(JsonUtil.transToLowerObject((JSONObject)jArray));
            }else if(jArray.getClass().toString().endsWith("JSONArray")){
                o2.add(JsonUtil.transToLowerArray((JSONArray)jArray));
            }
        }
        return o2;
    }
}