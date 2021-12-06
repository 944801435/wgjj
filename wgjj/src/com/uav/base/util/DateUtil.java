package com.uav.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DateUtil {
	private static final Logger log = Logger.getLogger(DateUtil.class);
	public static final String PATTERN_FULLTIME_COMPRESS="yyyyMMddHHmmss";
	public static final String PATTERN_FULLTIME="yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_TIME_MINUTE="yyyy-MM-dd HH:mm";
	public static final String PATTERN_TIME_MINUTE_SHORT="MM-dd HH:mm";
	public static final String STR_DATE="yyyy-MM-dd";
	public static final String STR_TIME="HH:mm";
	private static final String STR_FULL_TIME="yyyy-MM-dd HH:mm:ss";
	private static final String STR_FULL_TIME_YYYYMMDDHHMMSS="yyyyMMddHHmmss";
	private static final String STR_YYYY_MM_DD="yyyy年MM月dd日";
	public static final String PATTERN_TIME_WITH_MILSEC_COMPRESS="yyyyMMddHHmmssSSS";
	
	public static boolean isValidDate(String dateStr){
		Date date=DateUtil.parseStringToDate(dateStr);
		String str=DateUtil.parseDateToString(date);
		return str.equals(dateStr);
	}
	public static Date parseStringToDate(String sDate) {
		if (sDate==null || sDate.trim().length()==0)
			return null;
		else
			sDate=sDate.trim();
		
		try {
			return getDateFormat().parse(sDate);
		} catch (Exception e) {
			return null;
		}
	}
	public static String parseDateToString(Date date) {
		if(date!=null)
			return getDateFormat().format(date);
		else
			return "";
	}
	public static SimpleDateFormat getDateFormat(){
		return new SimpleDateFormat(STR_DATE);
	}
	
	public static boolean isValidFullTime(String fullTimeStr){
		Date date=DateUtil.parseStringToFullTime(fullTimeStr);
		String str=DateUtil.parseFullTimeToString(date);
		return str.equals(fullTimeStr);
	}
	public static Date parseStringToFullTime(String sDate) {
		if (sDate==null || sDate.trim().length()==0)
			return null;
		else
			sDate=sDate.trim();
		
		try {
			return getFullTimeFormat().parse(sDate);
		} catch (Exception e) {
			return null;
		}
	}
	public static String parseFullTimeToString(Date date) {
		if(date!=null)
			return getFullTimeFormat().format(date);
		else
			return "";
	}
	public static SimpleDateFormat getFullTimeFormat(){
		return new SimpleDateFormat(STR_FULL_TIME);
	}
	
	
	public static String formatDate(Date date,String pattern){
		if(date==null)
			return null;
		
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static Date parseDate(String sDate,String pattern) {
		if (sDate==null || sDate.trim().length()==0)
			return null;
		else
			sDate=sDate.trim();
		try {
			return new SimpleDateFormat(pattern).parse(sDate);
		} catch (Exception e) {
			log.error("DateUtil.parseDate(String sDate,String pattern)出现异常",e);
			return null;
		}
	}
	
	public static Date stringToDate(String dateStr,String pattern) throws ParseException{
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat format=new SimpleDateFormat(pattern);
		return format.parse(dateStr);
	}
	
	public static String formatStr(String dateStr,String form_pattern,String to_pattern) throws ParseException{
		if(StringUtils.isBlank(dateStr)){
			return "";
		}
		Date date=stringToDate(dateStr, form_pattern);
		return formatDate(date, to_pattern);
	}
	
	public static Date getNextSecond(Date now, int seconds){
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND,seconds);
		return cal.getTime();
	}
	
	public static Date getNextMinute(Date now, int minutes){
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE,minutes);
		return cal.getTime();
	}
	
	public static Date getNextDays(Date now, int days){
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.HOUR, days*24);
		return cal.getTime();
	}
	
	//由秒格式为时间
	public static String formatInt(Integer second){
		if(second<0){
			return "-1";
		}
		Integer time=(second%60>0 ? 1 : 0)+(second/60);
		int day=time/(60*24);
		int hour=(time%(60*24))/60;
		int minute=(time%(60*24))%60;
		return (day>0 ? (day+"天") : "")+(hour>0 ? (hour+"小时") : "")+(minute>0 ? (minute+"分钟") : "");
	}
	
	//判断日期为周几
	public static int weekNum(String dateStr) throws ParseException{
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(stringToDate(dateStr, "yyyyMMddHHmmss"));
		System.out.println(stringToDate(dateStr, "yyyyMMddHHmmss"));
		int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
		if(Calendar.MONDAY==weekDay){
			return 1;
		}else if(Calendar.TUESDAY==weekDay){
			return 2;
		}else if(Calendar.WEDNESDAY==weekDay){
			return 3;
		}else if(Calendar.THURSDAY==weekDay){
			return 4;
		}else if(Calendar.FRIDAY==weekDay){
			return 5;
		}else if(Calendar.SATURDAY==weekDay){
			return 6;
		}else if(Calendar.SUNDAY==weekDay){
			return 7;
		}else{
			return 0;
		}
	}
	
	/**
	 * 顺延后的时间
	 * @param dateStr   需要顺延的时间
	 * @param zqType	顺延的类型，工作日|自然日
	 * @param day	         顺延的天数
	 * @return
	 * @throws ParseException
	 */
//	public static String nextDay(String dateStr,String zqType,int day) throws ParseException{
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTime(stringToDate(dateStr, "yyyyMMdd"));
//		if(Constants.zq_type_gz.equals(zqType)){
//			while(day>0){
//				calendar.set(Calendar.HOUR, 1*24);
//				/*int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
//				if(Calendar.SATURDAY==weekDay || Calendar.SUNDAY==weekDay){
//					continue;
//				}*/
//				if(ConstantsUtil.holiday.contains(formatDate(calendar.getTime(), "yyyyMMdd"))){
//					continue;
//				}
//				day--;
//			}
//		}else{
//			calendar.set(Calendar.HOUR, day*24);
//		}
//		return formatDate(calendar.getTime(), "yyyyMMdd");
//		
//	}
	
	/**
	 * 顺延后的时间
	 * @param dateStr   需要顺延的时间
	 * @param zqType	顺延的类型，工作日|自然日
	 * @param day	         顺延的天数
	 * @return
	 * @throws ParseException
	 */
//	public static Date nextDay(Date date,String zqType,int day) throws ParseException{
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTime(date);
//		if(Constants.zq_type_gz.equals(zqType)){
//			while(day>0){
//				calendar.set(Calendar.HOUR, 1*24);
//				/*int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
//				if(Calendar.SATURDAY==weekDay || Calendar.SUNDAY==weekDay){
//					continue;
//				}*/
//				if(ConstantsUtil.holiday.contains(formatDate(calendar.getTime(), "yyyyMMdd"))){
//					continue;
//				}
//				day--;
//			}
//		}else{
//			calendar.set(Calendar.HOUR, day*24);
//		}
//		return calendar.getTime();
//		
//	}
	
	
	/**
	 * 两个时间差
	 */
	public static Integer days(Date date1,Date date2){
		Long times=date2.getTime()-date1.getTime();
		long days=times/1000/60/60/24;
		return Integer.parseInt(days+"");
	}
	
	public static Date getNowTime() {
		return new Date();
	}
	
	public static Date getNowDate() {
		return parseStringToDate(parseDateToString(getNowTime()));
	}
	
	public static String getNowDateString() {
		return parseDateToString(getNowTime());
	}

	public static String getNowFullTimeString() {
		return parseFullTimeToString(getNowTime());
	}
	
	public static long getDays(String begDateStr, String endDateStr) {
		Date begDate=DateUtil.parseStringToDate(begDateStr);
		Date endDate=DateUtil.parseStringToDate(endDateStr);
		return getDays(begDate, endDate);
	}
	
	public static long getDays(Date begDate, Date endDate) {
		return (endDate.getTime() - begDate.getTime())/(1000*24*60*60);
	}
	
	public static String getNowFullTimeStringyyyyMMddHHmmss() {
		return parseFullTimeToStringyyyyMMddHHmmss(getNowTime());
	}
	
	public static SimpleDateFormat getFullTimeFormatyyyyMMddHHmmss(){
		return new SimpleDateFormat(STR_FULL_TIME_YYYYMMDDHHMMSS);
	}
	
	public static String parseFullTimeToStringyyyyMMddHHmmss(Date date) {
		if (date==null)
			return "";
		else
			return getFullTimeFormatyyyyMMddHHmmss().format(date);
	}
	
	public static Date getNextDay(Date date, int days){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,days);
		return cal.getTime();
	}
	
	public static Date getNextMonth(Date date, int mons){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH,mons);
		return cal.getTime();
	}
	
	public static Date getNextYear(Date date, int years){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
	public static String formatYYYYMMDD(Date date) {
		return formatDate(date,STR_YYYY_MM_DD);
	}
	
	// time1 > time2 + seconds == true
	public static boolean compareTime(String time1, String time2, Integer seconds) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(time2, "yyyy-MM-dd HH:mm:ss"));
		cal.add(Calendar.SECOND, seconds);

		Date time1sum = parseDate(time1, "yyyy-MM-dd HH:mm:ss");
		Date time2sum = cal.getTime();
		if (time1sum.getTime() > time2sum.getTime()) {
			result = true;
		}
		return result;
	}
	
}
