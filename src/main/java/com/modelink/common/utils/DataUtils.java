package com.modelink.common.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class DataUtils {

    public static int tranform2Integer(String varchar){
        if(StringUtils.hasText(varchar)){
            return Integer.parseInt(varchar);
        }
        return 0;
    }
    public static BigDecimal tranform2BigDecimal(String varchar){
        if(StringUtils.hasText(varchar)){
            return new BigDecimal(varchar);
        }
        return new BigDecimal("0.00");
    }
}
