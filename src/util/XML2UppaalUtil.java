package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bean.Automata;
import bean.Location;
import bean.Template;
import bean.Transition;

public class XML2UppaalUtil {
	
	private static Automata automata;
	
	public XML2UppaalUtil(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] buf = new byte[is.available()];
			is.read(buf);
			this.automata = XStreamTransformUtil.toBean(Automata.class, buf);
		} catch (Exception e) {
			System.out.println("读取异常");
		} finally {
			try{
				is.close();
			} catch (Exception e) {
				System.out.println("关闭异常");
			}
		}
	}

	
	public static Automata getAutomata() {
		return automata;
	}
	
	
}
