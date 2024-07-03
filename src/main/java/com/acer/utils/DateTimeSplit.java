package com.acer.utils;

import java.util.ArrayList;

/**
 * 时间拆分
 */
public class DateTimeSplit {


    /**
     * 将dateStr字符串拆分成年月日存到list中（dateStr格式：年-月-日）
     * @param dateStr 要拆分的字符串
     */
    public static ArrayList<Integer> dateStringSplit(String dateStr){
        ArrayList<Integer> result = new ArrayList<>();
        try {
            for (String s : dateStr.split("-")) {
                result.add(Integer.valueOf(s));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return result;
    }
}
