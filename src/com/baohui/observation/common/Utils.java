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

	public static final String bathPath = "C:/Users/acer/Desktop/�̵�/";

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
	 * ��ȡϵͳ��ǰTS
	 * 
	 * @return
	 */
	public static Long getNow() {
		return new Date().getTime();
	}

	/**
	 * ��ȡϵͳ��ǰTS
	 * 
	 * @return
	 */
	public static Timestamp getTS() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * ��ȡָ��ʱ���TS
	 * 
	 * @return
	 */
	public static Timestamp getTS(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * ��ȡָ��ʱ���TS
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
	 * ����ʱ���ַ���
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(String format) {
		return getDateString(format, new Date());
	}

	/**
	 * ����ʱ���ַ���
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * ����������ƴ�ӳ��ַ���<br>
	 * ����:<br/>
	 * String str = jointToString({1, 2, 3}, "'");<br/>
	 * str = "'1', '2', '3'";
	 * 
	 * @param objList
	 *            �������飬������Ǽ򵥶�������ر�֤�������ʵ�ֵ�toString()����
	 * @param split
	 *            �ָ���
	 * @param quote
	 *            ��ע��������ķ���
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
	 * ����������ƴ�ӳ��ַ���<br>
	 * ����:<br/>
	 * String str = jointToString({1, 2, 3}, "'");<br/>
	 * str = "'1', '2', '3'";
	 * 
	 * @param objList
	 *            �������飬������Ǽ򵥶�������ر�֤�������ʵ�ֵ�toString()����
	 * @param split
	 *            �ָ���
	 * @param quote
	 *            ��ע��������ķ���
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String jointToString(List objList, String split, String quote) {
		return jointToString(objList.toArray(), split, quote);
	}

	/**
	 * ����������ƴ�ӳ��ַ�����ʹ�ö��ţ�,����Ϊ�ָ�����ӡ�ţ�"����Ϊ��ע<br>
	 * ����:<br/>
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
	 * ��һ��list��ֳ����ɸ�������С��list
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
