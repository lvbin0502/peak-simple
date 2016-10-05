package com.baohui.observation.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baohui.observation.common.Utils;
import com.baohui.observation.excel.ExcelImporter;
import com.baohui.observation.excel.FileImporter;
import com.baohui.observation.price.PriceRestrictObject;
import com.baohui.observation.price.RestrictPriceCheck;

public class LocalMainProcessor {

	private File excelFile; // �ϴ����ļ�
	private String excelFileFileName; // ����ԭʼ�ļ���

	private RestrictPriceCheck restrictPriceCheck = new RestrictPriceCheck();

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}

	public String process() {
		try {
			// ��ȡģ���ļ�
			FileImporter<PriceRestrictObject> im = initExcelImporter();

			// ��Ч�Լ���
			String errMsg = im.validate();
			if (!Utils.isBlank(errMsg)) {
				return errMsg;
			}

			// �����ļ�
			Map<String, Integer> propMapping = getPropMapping();
			List<PriceRestrictObject> list = im.parse(
					new PriceRestrictObject(), propMapping);

			// ִ�м��
			System.out.println("#################################################################");
			System.out.println("\t��ʼץȡ������رմ���");
			System.out.println("#################################################################");
			String resultFile = restrictPriceCheck.process(list, excelFileFileName);
			System.out.println("#################################################################");
			System.out.println("\tִ�гɹ�������ļ�Ϊ��" + resultFile);
			System.out.println("#################################################################");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "����ʧ��:" + e.getMessage();
		}
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileImporter<PriceRestrictObject> initExcelImporter()
			throws IOException {
		if (!excelFileFileName.endsWith("xls")) {
			throw new IOException("�ؼ��ļ�������xls��ʽ");
		}
		return new ExcelImporter<PriceRestrictObject>(new FileInputStream(
				excelFile), "template.xls");
	}

	/**
	 * @return
	 */
	private Map<String, Integer> getPropMapping() {
		Map<String, Integer> propMapping = new HashMap<String, Integer>();
		propMapping.put("artNo", 0);
		propMapping.put("price", 1);
		return propMapping;
	}
}
