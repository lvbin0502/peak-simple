/**
 * 
 */
package com.baohui.observation.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;


/**
 * ������Ʒ��������߼�����
 * 
 * @author lvbin 2015-8-12
 */
public class JDItemSearchObservation extends ItemSearchObservation{
	
	private Map<String, String> jdShopMap = new HashMap<String, String>();
	
	public JDItemSearchObservation(String keyword) {
		super(keyword);
	}

	public JDItemSearchObservation(String keyword, SearchSortParam sort) {
		super(keyword, sort);
	}

	@Override
	protected List<ItemData> parse(Document doc) {
		List<ItemData> list = new ArrayList<ItemData>();
		String buf = doc.html();
		// �޳��Ƽ����
		String item[] = buf.split("<li data-sku");
		for (int i = 1; i < item.length; i++) {
			String record = item[i];
			String title = Utils.parseInfo(record, "p-name", "<em>", "</em>");
			if(!title.contains("ƥ��")) {
				continue;// XXX ���˵�ƥ���޹صĲ�Ʒ
			}
			String id = Utils.parseInfo(record, "=", "\"", "\" class");
			String price = Utils.parseInfo(record, "p-price", "data-price=\"", "\">");
			String storeId = Utils.parseInfo(record, "p-shop", "data-shopid=\"", "\">");
			String storeName = null;
			if(!Utils.isBlank(storeId)) {
				storeName = getStoreName(storeId);
			} else {
				System.out.println("itemId = " + id);
				storeName = "δ֪����";
			}
			String sales = Utils.parseInfo(record, "comments-list", "\">", "</a>");
			ItemData data = new ItemData();
			data.setItemId(id);
			data.setPrice(Double.parseDouble(price));
			data.setStoreName(storeName);
			data.setSales(Integer.parseInt(sales));
			list.add(data);
		}
		return list;
	}

	/**
	 * @param storeId
	 * @return
	 */
	private String getStoreName(String storeId) {
		String existName = jdShopMap.get(storeId);
		if(!Utils.isBlank(existName)) {
			return existName;
		}
		// ��װurl
		String url = "http://mall.jd.com/index-"+storeId+".html";
		// ִ�в�ѯ
		String buf = new PageCapture(charset).getUrlContent(url);
		// ������ѯ���
		String name;
		try {
			int startIndex = buf.indexOf("meta content=") + "meta content=".length() + 1;
			name = buf.substring(startIndex, buf.indexOf(",", startIndex + 1));
//			name = buf.substring(buf.indexOf("	") + 1, buf.indexOf("- ����"));
			if(name == null) {
				return "δ֪����";
			}
			String storeName = name.trim();
			jdShopMap.put(storeId, storeName);
			return storeName;
		} catch (Exception e) {
			System.out.println("error in storeId: " + storeId);
			e.printStackTrace();
			return "δ֪����";
		}
		
	}

	/**
	 * @return
	 */
	@Override
	protected String buildSearchUrl() {
		if(Utils.isBlank(this.keyword)) {
			throw new RuntimeException("�ؼ��ֲ���Ϊ��");
		}
		//TODO ��������ʽ֧��
		String fmt = "http://search.jd.com/Search?keyword=ƥ��%1$s&enc=%2$s&wq=ƥ��%1$s&psort=2&qrst=unchange";
		String params = "";
		for (String p : otherParams) {
			params = params + "&" + p;
		}
		return String.format(fmt, keyword, charset);
	}

	@Override
	protected String getPlantForm() {
		return "jd_";
	}

	@Override
	protected boolean page404(Document doc) {
		// TODO Auto-generated method stub
		return false;
	}
}
