package com.jgc.crm.commns.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对Date类型进行处理的工具类
 */
public class DateUtils {

    public static String formateDateTime(Date date){
        /**对指定的date对象进行格式化
         *
         */
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr=simpleDateFormat.format(date);
        return dateStr;
    }
    public static String formateDate(Date date){
        /**对指定的date对象进行格式化
         *
         */
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd ");
        String dateStr=simpleDateFormat.format(date);
        return dateStr;
    }
    public static String formateTime(Date date){
        /**对指定的date对象进行格式化
         *
         */
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd ");
        String dateStr=simpleDateFormat.format(date);
        return dateStr;
    }
}
