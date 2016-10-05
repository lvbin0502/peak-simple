/**
 * 
 */
package com.baohui.observation.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvbin 2015-7-24
 */
public class Utils {

	public static final String bathPath = "C:/Users/acer/Desktop/盘点/";

	public static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String parseInfo(String buf, String prefix, String mid,
			String suffix) {
		int nameStartIndex = buf.indexOf(mid, buf.indexOf(prefix));
		int nameEndIndex = buf.indexOf(suffix, nameStartIndex);
		if (nameStartIndex == -1 || nameEndIndex == -1) {
			return "";
		}
		return buf.substring(nameStartIndex + mid.length(), nameEndIndex);
	}

	/**
	 * 获取系统当前TS
	 * 
	 * @return
	 */
	public static Long getNow() {
		return new Date().getTime();
	}

	/**
	 * 获取系统当前TS
	 * 
	 * @return
	 */
	public static Timestamp getTS() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 获取指定时间的TS
	 * 
	 * @return
	 */
	public static Timestamp getTS(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 获取指定时间的TS
	 * 
	 * @return
	 */
	public static Timestamp getTS(String dateString, String format) {
		try {
			return getTS(new SimpleDateFormat(format).parse(dateString));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 生成时间字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(String format) {
		return getDateString(format, new Date());
	}

	/**
	 * 生成时间字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 将对象数组拼接成字符串<br>
	 * 例如:<br/>
	 * String str = jointToString({1, 2, 3}, "'");<br/>
	 * str = "'1', '2', '3'";
	 * 
	 * @param objList
	 *            对象数组，如果不是简单对象，请务必保证对象的类实现的toString()方法
	 * @param split
	 *            分隔符
	 * @param quote
	 *            标注单个对象的符号
	 * @return
	 */
	public static String jointToString(Object[] objList, String split,
			String quote) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < objList.length; i++) {
			buffer.append(quote);
			buffer.append(objList[i].toString());
			buffer.append(quote);
			if (i != objList.length - 1) {
				buffer.append(split);
			}
		}
		return buffer.toString();
	}

	/**
	 * 将对象数组拼接成字符串<br>
	 * 例如:<br/>
	 * String str = jointToString({1, 2, 3}, "'");<br/>
	 * str = "'1', '2', '3'";
	 * 
	 * @param objList
	 *            对象数组，如果不是简单对象，请务必保证对象的类实现的toString()方法
	 * @param split
	 *            分隔符
	 * @param quote
	 *            标注单个对象的符号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String jointToString(List objList, String split, String quote) {
		return jointToString(objList.toArray(), split, quote);
	}

	/**
	 * 将对象数组拼接成字符串，使用逗号（,）作为分隔符，印号（"）作为标注<br>
	 * 例如:<br/>
	 * String str = jointToString({1, 2, 3});<br/>
	 * str = "'1', '2', '3'";
	 * 
	 * @param objList
	 * @return
	 */
	public static String jointToString(Object[] objList) {
		return jointToString(objList, ",", "'");
	}

	/**
	 * 将一个list拆分成若干个数据量小的list
	 * 
	 * @param list
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<List> pagination(List list, int pageSize) {
		List result = new ArrayList();
		int total = list.size();
		int pageCount = total / pageSize;
		for (int i = 0; i < pageCount; i++) {
			result.add(list.subList(i * pageSize, (i + 1) * pageSize));
		}
		if (total > pageSize * pageCount) {
			result.add(list.subList(pageSize * pageCount, total));
		}
		return result;
	}
}
