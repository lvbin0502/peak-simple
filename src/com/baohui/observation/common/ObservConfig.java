package com.baohui.observation.common;

public class ObservConfig {

	private static boolean printUrl = false;
	private static boolean printContent = false;
	private static int interval = 10000;

	public static boolean isPrintUrl() {
		return printUrl;
	}

	public static void setPrintUrl(boolean printUrl) {
		ObservConfig.printUrl = printUrl;
	}

	public static boolean isPrintContent() {
		return printContent;
	}

	public static void setPrintContent(boolean printContent) {
		ObservConfig.printContent = printContent;
	}

	public static int getInterval() {
		return interval;
	}

	public static void setInterval(int interval) {
		ObservConfig.interval = interval;
	}
}
