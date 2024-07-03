package com.acer.annotate;

import java.lang.annotation.*;

/**
 * 读取或写入excel表格时的页
 * @author acer
 */
@Documented
@Retention(RetentionPolicy.RUNTIME) // 由JVM 加载，包含在类文件中，在运行时可以被获取到
@Target({ElementType.TYPE}) // 应用于类、接口（包括注解类型）、枚举
public @interface ExcelSheetAnnotate {
    // 读取第几页（从0开始）
    int sheet();
}
