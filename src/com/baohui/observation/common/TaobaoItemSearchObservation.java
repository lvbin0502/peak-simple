/**
 * 
 */
package com.baohui.observation.common;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * �Ա���Ʒ��������߼�����
 * 
 * @author lvbin 2015-8-12
 */
public class TaobaoItemSearchObservation extends ItemSearchObservation{
	
	public TaobaoItemSearchObservation(String keyword) {
		super(keyword);
	}

	public TaobaoItemSearchObservation(String keyword, SearchSortParam sort) {
		super(keyword, sort);
	}

	/**
	 * @param buf
	 * @return
	 */
	@Override
	protected List<ItemData> parse(Document doc) {
		List<ItemData> list = new ArrayList<ItemData>();
		// �޳��Ƽ����
//		String part[] = buf.split("recommendAuctions");
//		
//		String item[] = part[0].split("\"similar\"");
//		for (int i = 1; i < item.length; i++) {
//			String record = item[i];
//			String id = Utils.parseInfo(record, "\"nid\"", ":\"", "\",");
//			String price = Utils.parseInfo(record, "view_price", ":\"", "\",");
//			String storeName = Utils.parseInfo(record, "nick", ":\"", "\",");
//			String sales = Utils.parseInfo(record, "view_sales", ":\"", "��");
//			ItemData data = new ItemData();
//			data.setItemId(id);
//			data.setPrice(Double.parseDouble(price));
//			data.setStoreName(storeName);
//			data.setSales(Integer.parseInt(sales));
//			list.add(data);
//		}
		return list;
	}

	/**
	 * @return
	 */
	@Override
	protected String buildSearchUrl() {
		if(Utils.isBlank(this.keyword)) {
			throw new RuntimeException("�ؼ��ֲ���Ϊ��");
		}
		//!FIXME ����ƥ��
		String fmt = "https://s.taobao.com/search?initiative_id=tbindexz_%1$s&sourceId=tb.index&search_type=item&q=ƥ��%2$s&_input_charset=%3$s&sort=%4$s&personalprice=off&fs=1";
		String params = "";
		for (String p : otherParams) {
			params = params + "&" + p;
		}
		return String.format(fmt, Utils.getDateString("yyyyMMdd"), keyword, charset, sort.getParam() + params);
	}

	@Override
	protected String getPlantForm() {
		return "taobao_";
	}

	@Override
	protected boolean page404(Document doc) {
		Elements ele = doc.getElementsByClass("item-not-found");
		return ele == null;
	}
}
