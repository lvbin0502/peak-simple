package com.baohui.observation.main;

import java.io.File;

import com.baohui.observation.common.ObservConfig;
import com.baohui.observation.common.Utils;

public class Main {

	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("参数错误，必须输入文件名");
			return;
		}
		for (String arg : args) {
			if(arg.equals("--help")) {
				System.out.println("#########################################");
				System.out.println("############# 操 作 说 明  #################");
				System.out.println("#########################################");
				System.out.println("--printUrl : 打印抓取过程中的URL");
				System.out.println("--printContent : 输出每次抓取的网页源代码");
				System.out.println("--interval=X : 设置抓取时间间隔，X是正整数");
				return;
			}
			if(arg.equalsIgnoreCase("--printUrl")) {
				System.out.println("设置：成功开启URL打印");
				ObservConfig.setPrintUrl(true);
			}
			if(arg.equalsIgnoreCase("--printContent")) {
				System.out.println("设置：成功开启网页抓取源码输出");
				ObservConfig.setPrintContent(true);
			}
			if(arg.contains("--interval=")) {
				String interval = arg.substring("--interval=".length());
				Integer val = 0;
				try {
					val = Integer.valueOf(interval.trim());
				} catch (NumberFormatException e) {
					System.out.println("参数错误！时间间隔必须是正整数!【你TMD输入了" + interval + "】");
					return;
				}
				if(val <= 0) {
					val = 10;
				}
				System.out.println("设置：成功设置抓取间隔为" + val + "秒");
				ObservConfig.setInterval(val * 1000);
			}
		}
		for (int i=0; i<args.length; i++) {
			System.out.println("arg " + i + "=" + args[i]);
		}
		String filePath = args[0];
		File file = new File(filePath);
		if(!file.exists()) {
			System.out.println("文件不存在：" + filePath);
			return;
		}
		
		LocalMainProcessor localPro = new LocalMainProcessor();
		localPro.setExcelFile(file);
		localPro.setExcelFileFileName(file.getName());
		String result = localPro.process();
		if(!Utils.isBlank(result)) {
			System.out.println("执行失败，" + result);
		}
	}
}
