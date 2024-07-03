package com.acer.utils;


import java.util.Date;
import java.util.List;

/**
 * 字符串工具类
 * @author acer
 */
public class StringUtils {


    /**
     * 判断字符串是否纯数字
     * @author acer
     * @date 15:57 2023/5/23
     * @param str 校验的字符串
     * @return boolean
     **/
    public static boolean numberCheck(String str){
        return org.apache.commons.lang3.StringUtils.isNumeric(str);
    }

    /**
     * 将集合拼接成字符串
     * @author acer
     * @date 16:00 2023/5/23
     * @param list 需要转换的集合
     * @param symbol 在集合每条数据后添加的符号
     * @return java.lang.String
     **/
    public static String listToString(List<String> list, String symbol){
        return org.apache.commons.lang3.StringUtils.join(list,symbol);
    }

    /**
     * 将String类型转成type解析后的类型
     * @author acer
     * @date 16:09 2023/5/26
     * @param type 数据类型
     * @param data 要转的数据
     * @return java.lang.Object
    **/
    public static Object strToObject(String type,String data){
        // 解析type
        String strType = type.substring(type.lastIndexOf(".")+1);

        switch (strType){
            case "String":{
                return data;
            }
            case "Integer":{
                return Integer.valueOf(data);
            }
            case "Date":{
                // 先转成Date，再返回
                return DateUtils.strToDate(data);
            }
            case "Long":{
                return Long.valueOf(data);
            }
            case "Boolean":{
                // 判断data：1代表true，0代表false
                if ("1".equals(data)){
                    return true;
                }
                return false;
            }
            default: {
                // 转换失败
                return null;
            }
        }
    }

    /**
     *
     * @author
     * @date 16:56 2023/5/26
     * @param type 数据类型(java.lang.Integer)
     * @return java.lang.Class
    **/
    public static Class strToType(String type){
        // 解析type
        String strType = type.substring(type.lastIndexOf(".")+1);

        switch (strType){
            case "String":{
                return String.class;
            }
            case "Integer":{
                return Integer.class;
            }
            case "Date":{
                // 先转成Date，再返回
                return Date.class;
            }
            case "Long":{
                return Long.class;
            }
            case "Boolean":{
                return Boolean.class;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Object o = strToType("java.lang.Integer");
        System.out.println(o);
    }


}
