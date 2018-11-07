package com.modelink.common.utils;

import com.modelink.common.enums.AgePartEnum;
import com.modelink.common.enums.DateTypeEnum;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class DataUtils {

    public static int tranform2Integer(String varchar){
        if("-".equals(varchar)){
            return 0;
        }
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
    public static int timeTranform2Second(String averageStayTime){
        int second = 0;
        if (averageStayTime.indexOf(":") < 0) {
            return second;
        }
        String[] timeArray = averageStayTime.split(":");
        if (timeArray.length == 3) {
            second += Integer.parseInt(timeArray[0]) * 3600;
            second += Integer.parseInt(timeArray[1]) * 60;
            second += Integer.parseInt(timeArray[2]);
        } else if (timeArray.length == 2) {
            second += Integer.parseInt(timeArray[0]) * 60;
            second += Integer.parseInt(timeArray[1]);
        }

        return second;
    }

    public static List<String> initDayList(String chooseDate) {
        List<String> dateList = new ArrayList<>();

        String[] dateArray = chooseDate.split(" - ");
        dateList.add(dateArray[0]);

        if(dateArray[0].equals(dateArray[1])){
            return dateList;
        }
        Date startDate = DateUtils.formatDate(dateArray[0], "yyyy-MM-dd");
        String nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        while(!dateArray[1].equals(nextDate)){
            if (!dateList.contains(nextDate)) {
                dateList.add(nextDate);
            }
            startDate = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
            nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        }
        if (!dateList.contains(nextDate)) {
            dateList.add(nextDate);
        }

        return dateList;
    }

    /**
     * 根据选择的日期初始化顺序日期列表
     * @param chooseDate
     * @param dateType
     * @return
     */
    public static List<String> initDateList(String chooseDate, int dateType){
        List<String> dateList = new ArrayList<>();

        String dateKey;
        String[] dateArray = chooseDate.split(" - ");
        if(dateType == DateTypeEnum.时.getValue()){
            for(int i=0; i<24; i++) {
                dateKey = i + ":00-" + (i+1) + ":00";
                if (!dateList.contains(dateKey)) {
                    dateList.add(dateKey);
                }
            }
            return dateList;
        }

        dateKey = getDateKeyByDateType(dateArray[0], dateType);
        if(!dateList.contains(dateKey)) {
            dateList.add(dateKey);
        }

        if(dateArray[0].equals(dateArray[1])){
            return dateList;
        }
        Date startDate = DateUtils.formatDate(dateArray[0], "yyyy-MM-dd");
        String nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        while(!dateArray[1].equals(nextDate)){
            dateKey = getDateKeyByDateType(nextDate, dateType);
            if (!dateList.contains(dateKey)) {
                dateList.add(dateKey);
            }
            startDate = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
            nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        }
        dateKey = getDateKeyByDateType(nextDate, dateType);
        if (!dateList.contains(dateKey)) {
            dateList.add(dateKey);
        }

        return dateList;
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

        if(dateType == DateTypeEnum.时.getValue()){
            for(int i=1; i<25; i++) {
                resultMap.put(i + "", initValue);
            }
            return resultMap;
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
        String resultDate;
        String format = "yyyyMMdd";
        try {
            Date date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            if (dateType == DateTypeEnum.年.getValue()) {
                format = "yyyy";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            } else if (dateType == DateTypeEnum.季度.getValue()) {
                format = "yyyy";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
                return DateUtils.formatDate(date, format) + "0" + DateUtils.getSeasonByDate(date);
            } else if (dateType == DateTypeEnum.月.getValue()) {
                format = "yyyyMM";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            } else if (dateType == DateTypeEnum.周.getValue()) {
                format = "yyyyww";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            } else if (dateType == DateTypeEnum.日.getValue()) {
                format = "yyyyMMdd";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            } else if (dateType == DateTypeEnum.时.getValue()) {
                format = "H";
                date = DateUtils.formatDate(dateString, "yyyy-MM-dd HH:mm:ss");
            }
            resultDate = DateUtils.formatDate(date, format);
        } catch (Exception e) {
            resultDate = "";
        }
        return resultDate;
    }

    public static Map<Integer, Double> initAgeMap(){
        Map<Integer, Double> ageMap = new HashMap<>(9);
        ageMap.put(AgePartEnum.from_0_5.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_5_18.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_18_25.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_25_30.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_30_35.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_35_40.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_40_50.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_50_55.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_55_100.getValue(), 0.00);
        return ageMap;
    }
}
