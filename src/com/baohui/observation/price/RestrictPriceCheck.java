/**
 * 
 */
package com.baohui.observation.price;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.baohui.observation.common.ItemData;
import com.baohui.observation.common.ItemSearchObservation;
import com.baohui.observation.common.JDItemSearchObservation;
import com.baohui.observation.common.ObservConfig;
import com.baohui.observation.common.SearchSortParam;
import com.baohui.observation.common.TaobaoItemSearchObservation;
import com.baohui.observation.common.Utils;

/**
 * 商品价格过滤器<br/>
 * 查看商品价格，过滤出低价商品
 * 
 * @author lvbin 2015-8-12
 */
public class RestrictPriceCheck {
	
	private boolean printUrl = false;
	private boolean printContent = false;
	
	private OutputStreamWriter writer;
	public static final String basePath = "out/";

	public String process(List<PriceRestrictObject> list, String fileName)
			throws IOException {
		String simpleName = fileName.substring(0, fileName.lastIndexOf("."));
		String outPutFile = basePath
				+ String.format("%1$s_%2$s.csv", simpleName,
						Utils.getDateString("MM-dd"));
		File file = new File(outPutFile);
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		writer = new OutputStreamWriter(new FileOutputStream(outPutFile), "GBK");
		for (int i = 0; i < list.size(); i++) {
			PriceRestrictObject obj = list.get(i);
			System.out.println("抓取..." + obj.getArtNo());
			List<ItemData> resultList = doFilter(obj.getArtNo(), obj.getPrice());
			// 输出结果
			outputToCsv(obj.getArtNo(), obj.getPrice(), resultList, i == 0);
			// 暂停一段时间，防止被淘宝拉黑
			try {
				Thread.sleep(ObservConfig.getInterval());
			} catch (InterruptedException e) {
			}
		}
		writer.flush();
		writer.close();
		return outPutFile;
	}

	private List<ItemData> doFilter(String keyword, double restrictPrice) {
		List<String> duplicateCheck = new ArrayList<String>();
		List<ItemData> resultList = new ArrayList<ItemData>();
		// 查询你妹的淘宝
		ItemSearchObservation tObserv = new TaobaoItemSearchObservation(
				keyword, SearchSortParam.PRICE_ASC);
		for (ItemData item : tObserv.process()) {
			if (item.getPrice() < restrictPrice) {
				item.setPlantform("淘宝");
				resultList.add(item);
				duplicateCheck.add(item.getItemId()); // 添加到判重记录中，防止出现重复记录
			}
		}
		try {
			Thread.sleep(ObservConfig.getInterval());
		} catch (InterruptedException e) {
		}
		// 查询天猫
		ItemSearchObservation tmObserv = new TaobaoItemSearchObservation(
				keyword, SearchSortParam.PRICE_ASC);
		tmObserv.addOtherParams("tab=mall");
		for (ItemData item : tmObserv.process()) {
			if (item.getPrice() < restrictPrice) {
				if (duplicateCheck.contains(item.getItemId())) {
					continue; // 去重淘宝搜索中出现的重复记录
				}
				item.setPlantform("淘宝");
				resultList.add(item);
			}
		}
		// 查询京东
		ItemSearchObservation jObserv = new JDItemSearchObservation(keyword);
		for (ItemData item : jObserv.process()) {
			if (item.getPrice() < restrictPrice) {
				item.setPlantform("京东");
				resultList.add(item);
			}
		}
		return resultList;
	}

	public void outputToCsv(String keyword, double restrictPrice,
			List<ItemData> list, boolean withHead) {
		try {
			if (withHead) {
				writer.append("平台, 旺旺昵称, 货号, 零售价, 总部限价, 超限比率, 商品链接\n");
			}
			for (ItemData item : list) {
				String fmt = "%s,%s,%s,%s,%s,%.2f%%,%s\n";
				double rate = (restrictPrice - item.getPrice()) / restrictPrice
						* 100;
				if (rate > 50) {
					continue;// FIXME 临时忽略超限太大的：基本上都是未匹配上的
				}
				String line = String.format(fmt, item.getPlantform(),
						item.getStoreName(), keyword, item.getPrice(),
						restrictPrice, rate,
						getItemUrl(item.getPlantform(), item.getItemId()));
				writer.append(line);
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param plantform
	 * @param itemId
	 * @return
	 */
	private String getItemUrl(String plantform, String itemId) {
		if ("淘宝".equals(plantform)) {
			return "https://item.taobao.com/item.htm?id=" + itemId;
		}
		if ("京东".equals(plantform)) {
			return "http://item.jd.com/" + itemId + ".html";
		} else {
			throw new RuntimeException("不支持的平台：" + plantform);
		}
	}
	
	public void setPrintUrl(boolean printUrl) {
		this.printUrl = printUrl;
	}
	
	public void setPrintContent(boolean printContent) {
		this.printContent = printContent;
	}
}
