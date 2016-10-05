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
 * 导入工具 抽象类
 * 
 * @author lvbin 2015-8-13
 */
public abstract class FileImporter<T> {

	protected static final String basePath = "source/";
	protected Integer headerRowNums = 1;// 表头所在行数
	protected String templateName;
	protected InputStream inputStream;

	/**
	 * 
	 * @param inputStream
	 *            输入文件
	 * @param templateName
	 *            模板名称
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
	 * 验证文件是否合法
	 * 
	 * @return 返回空表示验证通过，否则返回错误原因
	 */
	public String validate() throws IOException {
		// 加载预设模板
		FileInputStream in = new FileInputStream(basePath + templateName);
//		ResourceLoader loader = new DefaultResourceLoader();
//		Resource r = loader.getResource(basePath + templateName);
//		if (!r.exists()) {
//			throw new IOException("没找到资源文件:" + templateName);
//		}
		List<String> headerTpl = loadFileFormat(in);

		// 加载用户输入表格
		for (int i = 0; i < headerTpl.size(); i++) {
			String expect = headerTpl.get(i);
			String fact = getString(headerRowNums - 1, i);
			if (!expect.equals(fact)) {
				String fmt = "文件不合法，第%1$s列应该为%2$s，而导入文件是%3$s。";
				return String.format(fmt, (i + 1), expect, fact);
			}
		}
		return null;
//		// 加载预设模板
//		FileInputStream in = null;
//		in = new FileInputStream(basePath + templateName);
//		List<String> headerTmpl = loadFileFormat(in);
//		for (int i = 0; i < headerTmpl.size(); i++) {
//			String expect = headerTmpl.get(i);
//			String fact = getString(headerRowNums - 1, i);
//			if (!expect.equals(fact)) {
//				String fmt = "文件不合法，第%1$s列应该为%2$s，而导入文件是%3$s。";
//				return String.format(fmt, (i + 1), expect, fact);
//			}
//		}
//		return null;
	}

	protected abstract List<String> loadFileFormat(InputStream input)
			throws IOException;

	/**
	 * 解析表格，提取数据，生成对象list
	 * 
	 * @param t
	 *            对象实例
	 * @param propMapping
	 *            属性值和表格中的列数映射关系
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
	 * 利用java反射原理给字段赋值
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
