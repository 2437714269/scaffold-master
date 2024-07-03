package com.acer.utils;

import java.util.HashMap;

/**
 * 文件类型校验
 * @author acer
 */
public class FileTypeUtils {

    /**
     * 图片类型
     */
    private static HashMap<String,String> typeMap;
    static {
        typeMap = new HashMap<>();
        typeMap.put("jpg","jpg");
        typeMap.put("png","png");
        typeMap.put("webp","webp");
        typeMap.put("bmp","bmp");
        typeMap.put("tif","tif");
        typeMap.put("gif","gif");
        typeMap.put("apng","apng");
    }


    /**
     * 图片类型校验
     * @author acer
     * @date 17:15 2023/5/18
     * @param strings 要校验的图片
     * @return boolean
    **/
    public static boolean pictureTypeCheck(String... strings){
        for (String str : strings) {
            String type = str.split(".")[1];
            if (typeMap.get(type) == null){
                return false;
            }
        }
        return true;
    }
}
