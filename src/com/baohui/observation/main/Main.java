package com.baohui.observation.main;

import java.io.File;

import com.baohui.observation.common.ObservConfig;
import com.baohui.observation.common.Utils;

public class Main {

	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("�������󣬱��������ļ���");
			return;
		}
		for (String arg : args) {
			if(arg.equals("--help")) {
				System.out.println("#########################################");
				System.out.println("############# �� �� ˵ ��  #################");
				System.out.println("#########################################");
				System.out.println("--printUrl : ��ӡץȡ�����е�URL");
				System.out.println("--printContent : ���ÿ��ץȡ����ҳԴ����");
				System.out.println("--interval=X : ����ץȡʱ������X��������");
				return;
			}
			if(arg.equalsIgnoreCase("--printUrl")) {
				System.out.println("���ã��ɹ�����URL��ӡ");
				ObservConfig.setPrintUrl(true);
			}
			if(arg.equalsIgnoreCase("--printContent")) {
				System.out.println("���ã��ɹ�������ҳץȡԴ�����");
				ObservConfig.setPrintContent(true);
			}
			if(arg.contains("--interval=")) {
				String interval = arg.substring("--interval=".length());
				Integer val = 0;
				try {
					val = Integer.valueOf(interval.trim());
				} catch (NumberFormatException e) {
					System.out.println("��������ʱ����������������!����TMD������" + interval + "��");
					return;
				}
				if(val <= 0) {
					val = 10;
				}
				System.out.println("���ã��ɹ�����ץȡ���Ϊ" + val + "��");
				ObservConfig.setInterval(val * 1000);
			}
		}
		for (int i=0; i<args.length; i++) {
			System.out.println("arg " + i + "=" + args[i]);
		}
		String filePath = args[0];
		File file = new File(filePath);
		if(!file.exists()) {
			System.out.println("�ļ������ڣ�" + filePath);
			return;
		}
		
		LocalMainProcessor localPro = new LocalMainProcessor();
		localPro.setExcelFile(file);
		localPro.setExcelFileFileName(file.getName());
		String result = localPro.process();
		if(!Utils.isBlank(result)) {
			System.out.println("ִ��ʧ�ܣ�" + result);
		}
	}
}
