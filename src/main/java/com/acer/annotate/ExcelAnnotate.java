package com.acer.annotate;

import java.lang.annotation.*;

/**
 * Excel表注解，表示：需要存储列
 * @author acer
 */
@Target({ElementType.FIELD}) // 应用于属性（包括枚举中的常量）
@Retention(RetentionPolicy.RUNTIME) //由JVM 加载，包含在类文件中，在运行时可以被获取到
public @interface  ExcelAnnotate {
    // 用于表示列名
    String name();
}
