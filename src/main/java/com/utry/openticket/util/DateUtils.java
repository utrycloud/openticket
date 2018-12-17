package com.utry.openticket.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author MH
 *
 */
public class DateUtils {

	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};
	private static final ThreadLocal<DateFormat> df2 = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};
	/**
	 * 返回一个年月日的string类型的日期
	 * @param date
	 * @return
	 */
	public static String getFormetDate(Date date){
		return df.get().format(date);
	}
	/**
	 * 返回一个年月日 时分秒的string类型的日期
	 * @param date
	 * @return
	 */
	public static String getContentFormetDate(Date date){
		return df2.get().format(date);
	}
}
