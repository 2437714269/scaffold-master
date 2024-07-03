package com.acer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换
 * @author acer
 */
public class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");


    /**
     * 字符串日期转date
     * @author acer
     * @date 14:15 2023/4/26
     * @param str 日期字符串
     * @return java.util.Date
     **/
    public static Date strToDate(String str) {
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date转String
     * @author acer
     * @date 14:18 2023/4/26
     * @param date 时间
     * @return java.lang.String
     **/
    public static String dateToStr(Date date){
        return sdf.format(date);
    }


    public static void main(String[] args) throws ParseException {
        Date date = DateUtils.strToDate("2023.04.17 14:02:42");
        //2023.04.17 14:02:42
        //2022.03.26 10:07:58
        System.out.println(date);

        String s = DateUtils.dateToStr(new Date());
        System.out.println(s);


    }
}
