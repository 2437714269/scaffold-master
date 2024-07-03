package com.acer.utils;

import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import static com.acer.utils.CopyUtils.initial;

/**
 * 非空校验
 * @author acer
 */
public class CheckUtils {

    /**
     * 对象get属性非空验证
     * @author zhj
     * @date 10:02 2022/12/5
     * @param objects 动态数组要验证的类
     * @return boolean
     **/
    public static boolean dataNonNull(Object... objects){
        //便利动态数组
        for (Object object : objects) {
            //非空判断
            if (nonempty(objects)){
                return false;
            }
            //获取Picture类
            Class<? extends Object> objectClass = object.getClass();
            //获取picture下所有属性
            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    //获取当前属性的get方法：拼接get方法，get+首字母大写的属性
                    //调用titleCase转首字母大写
                    Method method = object.getClass().getMethod("get" + initial(field.getName()));
                    //执行get方法
                    Object invoke = method.invoke(object);
                    //判断非空
                    if (nonempty(invoke)){
                        return false;
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 集合校验
     * @author acer
     * @date 16:48 2023/4/13
     * @param list 要校验的集合
     * @return boolean
     **/
    public static boolean listVerify(List<MultipartFile> list){
        if (Objects.isNull(list) || list.size()<0){
            return true;
        }
        return false;
    }


    /**
     * 检查数据为空返回：true
     * @author zhj
     * @date 14:46 2022/12/2
     * @param objects 动态数组，要检测的数据
     * @return boolean
     **/
    public static boolean nonempty(Object... objects){
        //判断objects中的数据是否为空
        for (Object object : objects) {
            if (Objects.isNull(object)){
                return true;
            }
        }
        return false;
    }

    /**
     * 上传文件非空校验
     * @author acer
     * @date 8:51 2023/5/22
     * @param file 上传的文件
     * @return boolean
    **/
    public static boolean nonemptyFile(MultipartFile file){
        return "".equals(file.getOriginalFilename()) || file.getOriginalFilename() == null;
    }


}
