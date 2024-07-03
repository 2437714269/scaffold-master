package com.acer.utils;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author acer
 */
public class IoUtils {

    /**
     * 读取.txt文件
     * @author acer
     * @date 16:17 2023/5/23
     * @param path 绝对路径
     * @return java.lang.String
    **/
    public static String readTxt(String path) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 写入本地数据
     * @author acer
     * @date 16:21 2023/5/23
     * @param value 要写入的数据
     * @param path 本地绝对路径
    **/
    public static void writeInFile(String value,String path){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            IOUtils.write(value,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拷贝文件
     * @author acer
     * @date 16:24 2023/5/23
     * @param front 拷贝文件
     * @param after 拷贝新文件
     * @return boolean
    **/
    public static void fileCopy(String front,String after){
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(front);
            fileOutputStream = new FileOutputStream(after);
            IOUtils.copy(fileInputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件内容到字节数组
     * @author acer
     * @date 16:33 2023/5/23
     * @param path 读取文件路径
     * @return java.lang.Byte[]
    **/
    public static byte[] readBytes(String path){

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            return IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }






    public static void main(String[] args) {
        System.out.println(readTxt("C:\\Users\\24377\\Desktop\\download\\a.txt"));
    }
}
