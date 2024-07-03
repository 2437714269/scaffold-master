package com.acer.utils;

import lombok.extern.slf4j.Slf4j;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 对象拷贝工具
 * @author acer
 */
@Slf4j
public class CopyUtils {

    /**
     * 实体类对象等对象的拷贝(要拷贝对象中的属性 大于 拷贝生成新对象中的属性)
     * @author acer
     * @date 14:11 2023/2/1
     * @param oldObject 要拷贝的对象
     * @param newObject 拷贝生成新的对象
     * @return T
    **/
    public static <T> T objectCopy(T oldObject,T newObject){
        // 1.分别获取两个对象的class对象
        Class oldClass = oldObject.getClass();
        Class newClass = newObject.getClass();

        // 2.遍历newClass所有的属性
        Field[] declaredFields = newClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 设置私有属性可以访问
            declaredField.setAccessible(true);
            // 获取属性名
            String fieldName = declaredField.getName();
            try {
                // 调用splicingGetAndSetName方法进行拼接get或set方法
                String getMethodName = splicingGetAndSetName("get", fieldName);
                //获取oldObject中的fieldName的get方法
                Method oldClassMethod = oldClass.getMethod(getMethodName);
                // 执行get方法
                Object value = oldClassMethod.invoke(oldObject);

                // 获得属性描述器 如属性在父类：object.getClass().getSuperclass()
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName,newClass);
                // 获得set方法
                Method setMethod = propertyDescriptor.getWriteMethod();
                // 调用指定对象set方法
                setMethod.invoke(newObject, value);

            } catch (NoSuchMethodException e) {
                log.error("copy失败",e.getMessage());
                e.printStackTrace();
                //如果要copy的对象中有不同的对象直接返回空
                return null;
            } catch (InvocationTargetException e) {
                log.error("调用方法出错：{}",e.getMessage());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                log.error("调用方法出错：{}",e.getMessage());
                e.printStackTrace();
            } catch (IntrospectionException e) {
                log.error("获得属性描述器出错：{}",e.getMessage());
                e.printStackTrace();
            }

        }

        return newObject;
    }


    /**
     * 首字母转大写
     * @author acer
     * @date 15:31 2023/2/1
     * @param attribute 要转大写的字符串
     * @return java.lang.String
    **/
    public static String initial(String attribute){
        char[] chars = attribute.toCharArray();
        chars[0] -=32;
        return String.copyValueOf(chars);
    }

    /**
     * 拼接get或set方法
     * @author acer
     * @date 15:58 2023/2/1
     * @param prefix get或set
     * @param name 属性名
     * @return java.lang.String
    **/
    public static String splicingGetAndSetName(String prefix,String name){
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        //name首字母转大写
        String initial = initial(name);
        buffer.append(initial);
        return buffer.toString();
    }




}
