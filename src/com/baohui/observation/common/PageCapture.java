/**
 * 
 */
package com.baohui.observation.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lvbin 2015-5-21
 */
public class PageCapture {

	private int errCount = 0;
	private String charset = "utf-8";

	/**
	 * @param charset
	 *            the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	public PageCapture() {
	}

	public PageCapture(String charset) {
		super();
		this.charset = charset;
	}

	public String getUrlContent(String url) {
		try {
			return innerGetUrlContent(url);
		} catch (Exception e) {
			// e.printStackTrace();
			errCount++;
			if (errCount <= 20) {
				System.out.println("network error, retry times: " + errCount);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
				}
				return getUrlContent(url);
			} else {
				errCount = 0;
				throw new RuntimeException(e);
			}
		}
	}

	private String innerGetUrlContent(String url) {
		try {
			URL urlObj = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) urlObj
					.openConnection();
			httpConn.setConnectTimeout(3000);
			httpConn.setReadTimeout(10000);
			InputStreamReader input = new InputStreamReader(
					httpConn.getInputStream(), charset);
			BufferedReader bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
				contentBuf.append("\n");
			}
			return contentBuf.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Post Request
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String parameterData)
			throws Exception {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "gbk");
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(parameterData.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.flush();
			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception(
						"HTTP Request is not success, Response code is "
								+ httpURLConnection.getResponseCode());
			}
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);
			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

}