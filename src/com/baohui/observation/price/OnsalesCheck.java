/**
 * 
 */
package com.baohui.observation.price;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baohui.observation.common.ItemData;
import com.baohui.observation.common.TaobaoItemSearchObservation;
import com.baohui.observation.common.Utils;
import com.baohui.observation.excel.ExcelImporter;

/**
 * ��Ʒ�Ƿ��ϼܼ��<br/>
 * �鿴ͬ�м۸񣬱Ƚϵ��̼۸��Ƿ�������
 * 
 * @author lvbin 2015-8-12
 */
public class OnsalesCheck {

	private FileWriter fw = null;
	private String basePath = "D:/����/�޼�/";

	public void process(String fileName) {
		try {
			// ��ȡ���
			ExcelImporter<PriceRestrictObject> importer = new ExcelImporter<PriceRestrictObject>(
					new FileInputStream(basePath + fileName), "�޼۱�ģ��.xls");
			Map<String, Integer> propMapping = new HashMap<String, Integer>();
			propMapping.put("artNo", 0);
			propMapping.put("price", 1);
			List<PriceRestrictObject> list = importer.parse(
					new PriceRestrictObject(), propMapping);

			// ��ʼ������ļ�
			String simpleName = fileName.substring(0, fileName.indexOf("."));
			String outPutFileName = String.format("%1$s_�۸���_%2$s.csv",
					simpleName, Utils.getDateString("MM��dd��"));
			fw = new FileWriter(basePath + outPutFileName, true);

			// ������������
			for (int i = 0; i < list.size(); i++) {
				// ���Ա�����ȡ����
				PriceRestrictObject obj = list.get(i);
				List<ItemData> resultList = doFilter(obj.getArtNo(), obj
						.getPrice());
				// ������
				outputToCsv(obj.getArtNo(), !resultList.isEmpty(), i == 0);
				// ��ͣ10�룬��ֹ���Ա�����
				Thread.sleep(10000);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ItemData> doFilter(String keyword, double restrictPrice) {
		TaobaoItemSearchObservation observ = new TaobaoItemSearchObservation(keyword);
		observ.addOtherParams("tab=mall");
		return observ.process();
	}

	public void outputToCsv(String keyword, boolean isOnsales, boolean withHead) {
		try {
			if (withHead) {
				fw.write("����,��è���ϼ�,��ѯ����\n");
			}
			String viewUrl = "https://s.taobao.com/search?sort=sale-desc&q=ƥ��"
					+ keyword;
			String fmt = "%s,%s,%s\n";
			String line = String.format(fmt, keyword, isOnsales ? "��" : "��",
					viewUrl);
			fw.write(line);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
