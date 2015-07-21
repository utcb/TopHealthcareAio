package com.haiforce.tophealthcareaio.aiaole.util;

import android.content.Context;

import com.haiforce.tophealthcareaio.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private final static SimpleDateFormat longHourSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");;

	private final static SimpleDateFormat dateSdf = new SimpleDateFormat(
			"yyyy-MM-dd");
	private final static SimpleDateFormat dateMinte = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String chageTime(String timeStr) throws ParseException {
		if ("yyyy-MM-dd HH:mm:ss".length() == timeStr.length()) {
			Date date = longHourSdf.parse(timeStr);
			return dateMinte.format(date);
		} else {
			return timeStr;
		}

	}

	/**
	 * 获得本月的第一天，周一
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentMonthStartTime(String timeStr)
			throws ParseException {
		Date date = longHourSdf.parse(timeStr);
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		try {
			c.set(Calendar.DATE, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateSdf.format(c.getTime()) + " 00:00:00";
	}

	/**
	 * 获得本周的第一天，周一
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentWeekDayStartTime(String timeStr)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		Date date = longHourSdf.parse(timeStr);
		c.setTime(date);
		try {
			c.add(Calendar.DAY_OF_MONTH, -1); // 解决周日会出现 并到下一周的情况
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dateSdf.format(c.getTime()) + " 00:00:00";

	}

	/**
	 * 当前年的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentYearStartTime(String timeStr)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		Date date = longHourSdf.parse(timeStr);
		c.setTime(date);

		try {
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DATE, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateSdf.format(c.getTime()) + " 00:00:00";
	}

	/**
	 * 当前年的结束时间，即2012-12-31 23:59:59
	 * 
	 * @return
	 */
	public static String getCurrentYearEndTime(String timeStr)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		Date date = longHourSdf.parse(timeStr);
		c.setTime(date);

		try {
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateSdf.format(c.getTime()) + " 23:59:59";
	}

	/**
	 * 当前月的结束时间，即2012-01-31 23:59:59
	 * 
	 * @return
	 */
	public static String getCurrentMonthEndTime(String timeStr)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		Date date = longHourSdf.parse(timeStr);
		c.setTime(date);
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateSdf.format(c.getTime()) + " 23:59:59";
	}

	/**
	 * 获得本周的最后一天，周日
	 * 
	 * @return
	 */
	public static String getCurrentWeekDayEndTime(String timeStr)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		Date date = longHourSdf.parse(timeStr);
		c.setTime(date);
		try {
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			// 增加一个星期，才是我们中国人理解的本周日的日期
			c.add(Calendar.WEEK_OF_YEAR, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateSdf.format(c.getTime()) + " 23:59:59";
	}

	public static String getWeekByDate(Context context, String dateStr) {

		String weekStr = context.getResources().getString(R.string.weekStr);

		final String dayNames[] = weekStr.split(",");
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy.MM.dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {

				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {

				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static String getDateByStr(String dateStr) {

		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy.MM.dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);

		sdfInput = new SimpleDateFormat("MM-dd");
		return sdfInput.format(date);
	}

	// 转换时间
	public static String int2DateString(int dateline) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(dateline);
		String d = format.format(time);
		Date date = format.parse(d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	public static String getWeekEnd(Date dt, boolean showTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int move;
		if (w == 0) {
			move = 0;
		} else {
			move = 7 - w;
		}

		cal.add(cal.DATE, move);// 把日期往后增加一天.整数往后推,负数往前移动
		String dateString = sdf.format(cal.getTime());

		if (showTime) {
			sb.append(dateString);
			sb.append(" 23:59:59");
		} else {
			sb.append(sdf2.format(cal.getTime()));
		}

		return sb.toString();
	}

	public static String getWeekBeg(Date dt, boolean showTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");

		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int move;
		if (w == 0) {
			move = -6;
		} else {
			move = 1 - w;
		}

		cal.add(cal.DATE, move);// 把日期往后增加一天.整数往后推,负数往前移动
		String dateString = sdf.format(cal.getTime());

		if (showTime) {
			sb.append(dateString);
			sb.append(" 00:00:00");
		} else {
			sb.append(sdf2.format(cal.getTime()));
		}
		return sb.toString();
	}

}
