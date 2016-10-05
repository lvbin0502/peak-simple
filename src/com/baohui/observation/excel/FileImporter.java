/**
 * 
 */
package com.baohui.observation.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ���빤�� ������
 * 
 * @author lvbin 2015-8-13
 */
public abstract class FileImporter<T> {

	protected static final String basePath = "source/";
	protected Integer headerRowNums = 1;// ��ͷ��������
	protected String templateName;
	protected InputStream inputStream;

	/**
	 * 
	 * @param inputStream
	 *            �����ļ�
	 * @param templateName
	 *            ģ������
	 */
	public FileImporter(InputStream inputStream, String templateName) {
		this.templateName = templateName;
		this.inputStream = inputStream;
	}

	public Integer getHeaderRowNums() {
		return headerRowNums;
	}

	public void setHeaderRowNums(Integer headerRowNums) {
		this.headerRowNums = headerRowNums;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * ��֤�ļ��Ƿ�Ϸ�
	 * 
	 * @return ���ؿձ�ʾ��֤ͨ�������򷵻ش���ԭ��
	 */
	public String validate() throws IOException {
		// ����Ԥ��ģ��
		FileInputStream in = new FileInputStream(basePath + templateName);
//		ResourceLoader loader = new DefaultResourceLoader();
//		Resource r = loader.getResource(basePath + templateName);
//		if (!r.exists()) {
//			throw new IOException("û�ҵ���Դ�ļ�:" + templateName);
//		}
		List<String> headerTpl = loadFileFormat(in);

		// �����û�������
		for (int i = 0; i < headerTpl.size(); i++) {
			String expect = headerTpl.get(i);
			String fact = getString(headerRowNums - 1, i);
			if (!expect.equals(fact)) {
				String fmt = "�ļ����Ϸ�����%1$s��Ӧ��Ϊ%2$s���������ļ���%3$s��";
				return String.format(fmt, (i + 1), expect, fact);
			}
		}
		return null;
//		// ����Ԥ��ģ��
//		FileInputStream in = null;
//		in = new FileInputStream(basePath + templateName);
//		List<String> headerTmpl = loadFileFormat(in);
//		for (int i = 0; i < headerTmpl.size(); i++) {
//			String expect = headerTmpl.get(i);
//			String fact = getString(headerRowNums - 1, i);
//			if (!expect.equals(fact)) {
//				String fmt = "�ļ����Ϸ�����%1$s��Ӧ��Ϊ%2$s���������ļ���%3$s��";
//				return String.format(fmt, (i + 1), expect, fact);
//			}
//		}
//		return null;
	}

	protected abstract List<String> loadFileFormat(InputStream input)
			throws IOException;

	/**
	 * ���������ȡ���ݣ����ɶ���list
	 * 
	 * @param t
	 *            ����ʵ��
	 * @param propMapping
	 *            ����ֵ�ͱ���е�����ӳ���ϵ
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> parse(T t, Map<String, Integer> propMapping)
			throws Exception {
		List<T> list = new ArrayList<T>();
		for (int i = headerRowNums; i < getRowNums() + 1; i++) {
			T obj = (T) t.getClass().newInstance();
			for (String propName : propMapping.keySet()) {
				setFieldValue(obj, propName, i, propMapping.get(propName));
			}
			list.add(obj);
		}
		return list;
	}

	protected abstract Integer getRowNums();

	/**
	 * ����java����ԭ����ֶθ�ֵ
	 * 
	 * @param obj
	 * @param propName
	 * @param row
	 * @param integer
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private void setFieldValue(T obj, String propName, Integer rowNums,
			Integer index) throws Exception {
		Field field = obj.getClass().getDeclaredField(propName);
		String fieldType = field.getType().getSimpleName();
		field.setAccessible(true);
		if (fieldType.equals("String")) {
			field.set(obj, getString(rowNums, index));
		} else if (fieldType.equals("Integer") || fieldType.equals("int")) {
			field.set(obj, getInteger(rowNums, index));
		} else if (fieldType.equals("Double") || fieldType.equals("double")
				|| fieldType.equals("Float") || fieldType.equals("float")) {
			field.set(obj, getDouble(rowNums, index));
		} else if (fieldType.equals("Date")) {
			field.set(obj, getDate(rowNums, index));
		}
	}

	public abstract Object getDate(Integer rowNums, Integer index);

	public abstract String getString(Integer rowNums, int index);

	public abstract Integer getInteger(Integer rowNums, int index);

	public abstract Double getDouble(Integer rowNums, int index);
}
