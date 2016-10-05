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

	private File excelFile; // 上传的文件
	private String excelFileFileName; // 保存原始文件名

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
			// 获取模板文件
			FileImporter<PriceRestrictObject> im = initExcelImporter();

			// 有效性检验
			String errMsg = im.validate();
			if (!Utils.isBlank(errMsg)) {
				return errMsg;
			}

			// 解析文件
			Map<String, Integer> propMapping = getPropMapping();
			List<PriceRestrictObject> list = im.parse(
					new PriceRestrictObject(), propMapping);

			// 执行监控
			System.out.println("#################################################################");
			System.out.println("\t开始抓取，请勿关闭窗口");
			System.out.println("#################################################################");
			String resultFile = restrictPriceCheck.process(list, excelFileFileName);
			System.out.println("#################################################################");
			System.out.println("\t执行成功，结果文件为：" + resultFile);
			System.out.println("#################################################################");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "处理失败:" + e.getMessage();
		}
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileImporter<PriceRestrictObject> initExcelImporter()
			throws IOException {
		if (!excelFileFileName.endsWith("xls")) {
			throw new IOException("控价文件必须是xls格式");
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
