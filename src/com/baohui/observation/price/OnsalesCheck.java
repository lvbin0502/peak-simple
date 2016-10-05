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
 * 商品是否上架检查<br/>
 * 查看同行价格，比较店铺价格是否有优势
 * 
 * @author lvbin 2015-8-12
 */
public class OnsalesCheck {

	private FileWriter fw = null;
	private String basePath = "D:/电商/限价/";

	public void process(String fileName) {
		try {
			// 读取表格
			ExcelImporter<PriceRestrictObject> importer = new ExcelImporter<PriceRestrictObject>(
					new FileInputStream(basePath + fileName), "限价表模板.xls");
			Map<String, Integer> propMapping = new HashMap<String, Integer>();
			propMapping.put("artNo", 0);
			propMapping.put("price", 1);
			List<PriceRestrictObject> list = importer.parse(
					new PriceRestrictObject(), propMapping);

			// 初始化结果文件
			String simpleName = fileName.substring(0, fileName.indexOf("."));
			String outPutFileName = String.format("%1$s_价格监控_%2$s.csv",
					simpleName, Utils.getDateString("MM月dd日"));
			fw = new FileWriter(basePath + outPutFileName, true);

			// 逐条处理数据
			for (int i = 0; i < list.size(); i++) {
				// 从淘宝网拉取数据
				PriceRestrictObject obj = list.get(i);
				List<ItemData> resultList = doFilter(obj.getArtNo(), obj
						.getPrice());
				// 输出结果
				outputToCsv(obj.getArtNo(), !resultList.isEmpty(), i == 0);
				// 暂停10秒，防止被淘宝拉黑
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
				fw.write("货号,天猫已上架,查询链接\n");
			}
			String viewUrl = "https://s.taobao.com/search?sort=sale-desc&q=匹克"
					+ keyword;
			String fmt = "%s,%s,%s\n";
			String line = String.format(fmt, keyword, isOnsales ? "是" : "否",
					viewUrl);
			fw.write(line);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
