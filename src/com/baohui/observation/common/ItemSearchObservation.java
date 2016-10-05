/**
 * 
 */
package com.baohui.observation.common;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.baohui.observation.enhance.BookMetadataCaptureException;
import com.baohui.observation.price.RestrictPriceCheck;

/**
 * ��Ʒ��������߼�����
 * 
 * @author lvbin 2015-8-12
 */
public abstract class ItemSearchObservation {

	protected String keyword;
	protected String charset;
	protected SearchSortParam sort;
	protected List<String> otherParams = new ArrayList<String>();
	
	private static final int ERR_RETRY_COUNT = 3;
	private static final int TIMEOUT = 6000;
	private static final int TIMEOUT_SLEEP = 5000;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public SearchSortParam getSort() {
		return sort;
	}

	public void setSort(SearchSortParam sort) {
		this.sort = sort;
	}

	public ItemSearchObservation() {
		setDefaultParams();
	}

	public void addOtherParams(String param) {
		this.otherParams.add(param);
	}

	public ItemSearchObservation(String keyword) {
		this.keyword = keyword;
		setDefaultParams();
	}

	public ItemSearchObservation(String keyword, SearchSortParam sort) {
		this.keyword = keyword;
		this.sort = sort;
		setDefaultParams();
	}

	protected void setDefaultParams() {
		if (this.charset == null) {
			this.charset = "utf-8";
		}
		if (this.sort == null) {
			this.sort = SearchSortParam.DEFAULT;
		}
	}

	public List<ItemData> process() {
		// ��װurl
		String url = buildSearchUrl();
		if(ObservConfig.isPrintUrl()) {
			System.out.println("url="+url + "\r\n");
		}
		
		// ִ�в�ѯ������response��װ��Document����
		Document doc = doRequestUrl(url);
		// ͼ�鲻����
		if (page404(doc)) {
			return null;
		}
		
		if (ObservConfig.isPrintContent()) {
			// ���ԭʼ�ļ�
			try {
				String fileName = RestrictPriceCheck.basePath + "/" + keyword
						+ getPlantForm() + ".html";
				FileWriter fw = new FileWriter(fileName);
				fw.write(doc.html());
				fw.flush();
				fw.close();
			} catch (IOException e) {
			}
		}
		// ������ѯ���
		return parse(doc);
	}
	
	/**
	 * ����URL������response��װ��Document����
	 * 
	 * @param pageUrl
	 * @return
	 * @throws Exception
	 */
	protected Document doRequestUrl(String pageUrl) {
		URL url;
		try {
			url = new URL(pageUrl);
		} catch (MalformedURLException e2) {
			throw new BookMetadataCaptureException("����ץȡ�����з�������" + e2.getMessage(), e2);
		}
		int errCount = 0;
		Exception ex = null;
		// ������糬ʱ���������޴γ���
		while (errCount < ERR_RETRY_COUNT) {
			try {
				Document doc = Jsoup.connect(pageUrl).header("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(TIMEOUT).get();
				return doc;
			} catch (Exception e) {
				errCount++;
				ex = e;
				try {
					Thread.sleep(TIMEOUT_SLEEP);
				} catch (Exception e1) {// sleep quietly
				}
			}
		}
		throw new BookMetadataCaptureException("����ץȡ�����з�������" + ex.getMessage(), ex);
	}
	
	/**
	 * �ж�ץȡ����Ʒ�Ƿ����
	 * 
	 * @param doc
	 * @return
	 */
	protected abstract boolean page404(Document doc);

	/**
	 * @param doc
	 * @return
	 */
	protected abstract List<ItemData> parse(Document doc);

	/**
	 * @return
	 */
	protected abstract String buildSearchUrl();

	protected abstract String getPlantForm();
}
