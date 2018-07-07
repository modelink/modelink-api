package com.modelink.common.utils;

import com.modelink.common.annotation.ExportField;
import com.modelink.reservation.bean.Insurance;

import java.lang.reflect.Field;

/**
 * 反射相关工具类
 */
public class ClassReflectUtils {

    /**
     * 根据属性名获取属性值
     * @param fieldName 属性名
     * @param instant 类对象
     * @param clazz 类类型
     * @return
     * @throws Exception
     */
    public static <T> Object getValueByFieldName(String fieldName, T instant, Class<T> clazz) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        // 设置访问权限（这点对于有过android开发经验的可以说很熟悉）
        field.setAccessible(true);

        // 得到私有的变量值
        Object fieldValue = field.get(instant);
        // 输出私有变量的值
        return fieldValue;
    }

    /**
     * 获取导出注解
     * @param fieldName
     * @param beanClazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ExportField getAnnotationByFieldName(String fieldName, Class<T> beanClazz) throws Exception {
        Field field = beanClazz.getDeclaredField(fieldName);// 暴力获取private修饰的成员变量
        ExportField fieldAnnotation = field.getAnnotation(ExportField.class);
        return fieldAnnotation;
    }

    public static void main(String[] args) throws Exception {
        ExportField annotation = ClassReflectUtils.getAnnotationByFieldName("name", Insurance.class);
        System.out.println(annotation.value());
    }
}
