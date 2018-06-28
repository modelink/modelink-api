package com.modelink.common.utils;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;

/**
 * 反射相关工具类
 */
public class ClassReflectUtils {

    /**
     * 根据属性名获取属性值
     * @param propertyName 属性名
     * @param instant 类对象
     * @param clazz 类类型
     * @return
     * @throws Exception
     */
    public static Object getValueByPropertyName(String propertyName, T instant, Class<T> clazz) throws Exception {
        Field field = clazz.getDeclaredField(propertyName);
        // 设置访问权限（这点对于有过android开发经验的可以说很熟悉）
        field.setAccessible(true);

        // 得到私有的变量值
        Object propertyValue = field.get(instant);
        // 输出私有变量的值
        return propertyValue;
    }
}
