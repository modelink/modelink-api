package com.modelink.common.utils;

import com.modelink.common.enums.DateTypeEnum;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    /**
     * 根据选择的日期初始化Map集合
     * @param chooseDate
     * @param dateType
     * @return
     */
    public static Map<String, Object> initResultMap(String chooseDate, int dateType, String dataType){
        Map<String, Object> resultMap = new HashMap<>();
        String[] dateArray = chooseDate.split(" - ");

        Object initValue = "";
        if("int".equals(dataType)){
            initValue = 0;
        }else if("double".equals(dataType)){
            initValue = 0.00;
        }

        String dateKey = getDateKeyByDateType(dateArray[0], dateType);
        resultMap.put(dateKey, initValue);

        if(dateArray[0].equals(dateArray[1])){
            return resultMap;
        }
        Date startDate = DateUtils.formatDate(dateArray[0], "yyyy-MM-dd");
        String nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        while(!dateArray[1].equals(nextDate)){
            dateKey = getDateKeyByDateType(nextDate, dateType);
            resultMap.put(dateKey, initValue);
            startDate = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
            nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        }
        dateKey = getDateKeyByDateType(nextDate, dateType);
        resultMap.put(dateKey, initValue);
        return resultMap;
    }
    public static String getDateKeyByDateType(String dateString, int dateType){
        String format = "yyyyMMdd";
        Date date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
        if(dateType == DateTypeEnum.年.getValue()){
            format = "yyyy";
        }else if(dateType == DateTypeEnum.季度.getValue()){
            format = "yyyy";
            return DateUtils.formatDate(date, format) + "0" + DateUtils.getSeasonByDate(date);
        }else if(dateType == DateTypeEnum.月.getValue()){
            format = "yyyyMM";
        }else if(dateType == DateTypeEnum.周.getValue()){
            format = "yyyyww";
        }else if(dateType == DateTypeEnum.日.getValue()){
            format = "yyyyMMdd";
        }
        return DateUtils.formatDate(date, format);
    }
}
