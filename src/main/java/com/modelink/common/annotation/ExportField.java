package com.modelink.common.annotation;

import java.lang.annotation.*;

@Documented //文档生成时，该注解将被包含在javadoc中，可去掉
@Target(ElementType.FIELD)//目标是方法还是属性
@Retention(RetentionPolicy.RUNTIME) //注解会在class中存在，运行时可通过反射获取
@Inherited
public @interface ExportField {
    /** 导出时的列名 **/
    String value() default "";
    /** 数据类型，默认为0-字符串；1-日期； **/
    int dataType() default 0;
    /** 当数据类型为日期时的格式化字符 **/
    String format() default "yyyy-MM-dd";
}