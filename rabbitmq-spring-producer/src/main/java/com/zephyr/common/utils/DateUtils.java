package com.zephyr.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * @description  日期相关操作
 * @create 2015-10-23
 */
public class DateUtils {
	public static ThreadLocal<DateFormat> YYYY_MM_DD_MM_HH_SS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	public static ThreadLocal<DateFormat> YYYY_MM_DD = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	public static ThreadLocal<DateFormat> YYYY年MM月DD日 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy年MM月dd日");
		}
	};

	public static ThreadLocal<DateFormat> YYYY_MM = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM");
		}
	};

	public static ThreadLocal<DateFormat> YYYY_MM_DD_HH = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHH");
		}
	};

	public static ThreadLocal<DateFormat> YYYYMMDDMMHHSSSSS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmssSSS");
		}
	};

	public static ThreadLocal<DateFormat> YYMMDDMMHHSSSSS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyMMddHHmmssSSS");
		}
	};

	public static ThreadLocal<DateFormat> YYYYMMDDHHMMSS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};

	public static ThreadLocal<DateFormat> YYYY_MM_DD_HH_MM = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};

	public static ThreadLocal<DateFormat> YYYYMMDD = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};

	public static ThreadLocal<DateFormat> HHMMSS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("HHmmss");
		}
	};

	public static ThreadLocal<DateFormat> HHMMSS2 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		}
	};

	public static ThreadLocal<DateFormat> MM月DD日 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("MM月dd日");
		}
	};

	public static ThreadLocal<DateFormat> CRON_EXPRESSION = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("ss mm HH dd MM ? yyyy");
		}
	};

	public static Date parseToDate(String s) throws ParseException {
		return YYYY_MM_DD_MM_HH_SS.get().parse(s);
	}
	
	public static String ParseToString(Date date) 	{
		return YYYY_MM_DD_MM_HH_SS.get().format(date);
	}

	/**
	 * 当前日期的开始时间，即当前日的0点0分
	 * @param date
	 * @return
	 */
	public static Date getDayBegin(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 当前日期的结束时间，即当前日的23点59分
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 当前日期所在周的第一天(默认一周是从周日到周六)
	 * @param date
	 * @return
	 */
	public static Date getWeekBegin(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayBegin(date);
		c.setTime(date);
		int curdow =c.get(Calendar.DAY_OF_WEEK);
		int curdoy =c.get(Calendar.DAY_OF_YEAR);
		curdoy = curdoy - curdow + 1;
		c.set(Calendar.DAY_OF_YEAR, curdoy);
		
		return c.getTime();
	}

	/**
	 * 当前日期所在周的第七天(默认一周是从周日到周六)
	 * @param date
	 * @return
	 */
	public static Date getWeekEnd(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayEnd(date);
		c.setTime(date);
		int curdow =c.get(Calendar.DAY_OF_WEEK);
		int curdoy =c.get(Calendar.DAY_OF_YEAR);
		curdoy =  curdoy - curdow + 7;
		c.set(Calendar.DAY_OF_YEAR,curdoy);
		
		return c.getTime();
	}

	/**
	 * 当前日期所在的月的第一天
	 * @param date
	 * @return
	 */
	public static Date getMonthBegin(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayBegin(date);
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 当前日期所在月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayEnd(date);
		c.setTime(date);
		int curmon = c.get(Calendar.MONTH);
		//加一个月
		c.set(Calendar.MONTH, curmon + 1);
		//加一个月后的第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		int curdoy = c.get(Calendar.DAY_OF_YEAR);
		//上一天
		c.set(Calendar.DAY_OF_YEAR, curdoy - 1);
		return c.getTime();
	}

	/**
	 * 当前日期所在季度的第一天
	 * @param date
	 * @return
	 */
	public static Date getSeasonBegin(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayBegin(date);
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch(month){
		case 0:
		case 1:
		case 2:{
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			break;
		}
		case 3:
		case 4:
		case 5:{
			c.set(Calendar.MONTH, 3);
			c.set(Calendar.DAY_OF_MONTH, 1);
			break;
		}
		case 6:
		case 7:
		case 8:{
			c.set(Calendar.MONTH, 6);
			c.set(Calendar.DAY_OF_MONTH, 1);
			break;
		}
		case 9:
		case 10:
		case 11:{
			c.set(Calendar.MONTH, 9);
			c.set(Calendar.DAY_OF_MONTH, 1);
			break;
		}
		}
		return c.getTime();
	}

	/**
	 * 当前日期所在季度的最后一天
	 * @param date
	 * @return
	 */
	public static Date getSeasonEnd(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayEnd(date);
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch(month){
		case 0:
		case 1:
		case 2:{
			c.set(Calendar.MONTH, 3);
			c.add(Calendar.DAY_OF_YEAR, -1);
			break;
		}
		case 3:
		case 4:
		case 5:{
			c.set(Calendar.MONTH, 5);
			c.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 6:
		case 7:
		case 8:{
			c.set(Calendar.MONTH, 9);
			c.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}
		case 9:
		case 10:
		case 11:{
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		}
		return c.getTime();
	}

	/**
	 * 当前日期所在年的第一天
	 * @param date
	 * @return
	 */
	public static Date getYearBegin(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayBegin(date);
		c.setTime(date);
		//第一天
		c.set(Calendar.DAY_OF_YEAR,1);
		return c.getTime();
	}

	/**
	 * 当前日期所在年的最后一天
	 * @param date
	 * @return
	 */
	public static Date getYearEnd(Date date){
		Calendar c = Calendar.getInstance();
		date = getDayEnd(date);
		c.setTime(date);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 31);
		return c.getTime();
	}

	/**
	 * 获取当前时间的间隔天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		
		return c.getTime();
	}
	
	/**
	 * 当前日期加上传入的时分秒时间
	 * date为一个日期，默认时间为00:00:00
	 * time为任意日期，只有时分秒有意义
	 */
	public static Date addTime(Date date,Date time){
		Calendar c = Calendar.getInstance();
		Calendar t = Calendar.getInstance();
		c.setTime(date);
		t.setTime(time);
		int mins =  t.get(Calendar.MINUTE);
		int hour = t.get(Calendar.HOUR_OF_DAY);
		c.add(Calendar.HOUR_OF_DAY,hour);
		c.add(Calendar.MINUTE,mins);
		
		return c.getTime();
	}
	
	/**
	 * 获取当前时间的间隔周数
	 */
	public static Date addWeek(Date date,int week){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, week * 7);
		
		return c.getTime();
	}
	
	/**
	 * 获取当前时间的间隔月数
	 */
	public static Date addMonth(Date date,int month){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		
		return c.getTime();
	}
	
	/**
	 * 获取当前时间的间隔月数
	 */
	public static Date addYear(Date date,int year){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		
		return c.getTime();
	}
	
	/**
	 * 获取当前时间的间隔季度
	 */
	public static Date addSeason(Date date,int season){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		//当前月份
		int curmon = c.get(Calendar.MONTH);
		//减去季度的倍数
		curmon = curmon + 3 * season;
		c.add(Calendar.MONTH, curmon);
		
		return c.getTime();
	}
     /**
      * 日期计算
      * Calendar.MONTH，
      * Calendar.DATE
      * Calendar.HOUR
      * Calendar.MINUTE
      */
	public static Date CalcDate(Date date,int calcType,int val) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		//c.add(Calendar.MONTH, val);		
		c.add(calcType, val);
		return c.getTime();
		
	}
	/**
	 * 计算日期
	 */
	public static Long CalcDatetoNum(Date date1,Date date2,int calcType){
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date1);
		Long d1= cal.getTimeInMillis();
		cal.setTime(date2);
		Long d2= cal.getTimeInMillis();
		
		Long calcTypeNum;
		switch (calcType) {
		case Calendar.MINUTE:
			calcTypeNum=(long)1000*60;
			break;
		case Calendar.HOUR:
			calcTypeNum=(long)1000*3600;
			break;
		case Calendar.DATE:
			calcTypeNum=(long)1000*3600*24;
			break;
		default:
			calcTypeNum=1000L;
			break;
		}
		return (d2-d1)/calcTypeNum;
		
	}
	
	/**
	 * 计算date1和date2间隔几个月
	 * @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(Date date1, Date date2){
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		result += year * 12 + 1;
		return result;

	}

	/**
	 * 获取当前日期的星期
	 * @param dt
	 * @return
	 */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
			w = 0;
		}
        return weekDays[w];
    }
    
	public static boolean isValidDate(String str,String formatString) {
	  	boolean convertSuccess=true;
	   	SimpleDateFormat format = new SimpleDateFormat(formatString);
	   	try {
		  format.setLenient(false);
		  format.parse(str);
	   	} catch (ParseException e) {
		   convertSuccess=false;
	   	}
	   	return convertSuccess;
	}

	/**
	 * 返回日时分秒
	 * @param second
	 * @return
	 */
	public static String secondToTime(long second) {
		//转换天数
		long days = second / 86400;
		//剩余秒数
		second = second % 86400;
		//转换小时数
		long hours = second / 3600;
		//剩余秒数
		second = second % 3600;
		//转换分钟
		long minutes = second / 60;
		//剩余秒数
		second = second % 60;
		if (0 < days){
			return days + "天"+hours+"小时"+minutes+"分"+second+"秒";
		}else {
			return String.format("%02d", hours)+":"+String.format("%02d", minutes)+":"+String.format("%02d", second);
		}
	}

	/***
	 * 计算时间差并转换格式
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static String transferTime(Date date1, Date date2) {
		Long second = CalcDatetoNum(date1, date2, -1);
		return secondToTime(second);
	}

	/**
	 * 分钟加减
	 * @param date
	 * @param minute 负数：提前多少分钟，正数：往后延长多少分钟
	 * @return
	 */
	public static Date addMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}

	public static String getCronExpression(Date date) {
		return CRON_EXPRESSION.get().format(date);
	}

//	public static void main(String[] args) throws ParseException {
//		//System.out.println(transferTime(parseToDate("2018-09-11 10:05:11"), parseToDate("2018-09-11 10:58:20")));
//		System.out.println(getCronExpression(new Date()));
//	}
}
