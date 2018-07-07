package com.modelink.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    public static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 在指定日期上增加 offsetValue 时间，并以 pattern 的形式格式化
     * @param date 指定的时间
     * @param dateType 时间格式（如Calendar.DAY_OF_MONTH）
     * @param offsetValue
     * @param pattern
     * @return
     */
    public static String calculateDate(Date date, int  dateType, int offsetValue, String pattern) {
        //使用默认时区和语言环境获得一个日历。
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //时间加减
        calendar.add(dateType, offsetValue);
        //通过格式化输出日期
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(calendar.getTime());
    }

    /**
     * 在指定日期上增加 offsetValue 时间
     * @param date
     * @param dateType
     * @param offsetValue
     * @return
     */
    public static Date calculateDate(Date date, int  dateType, int offsetValue) {
        //使用默认时区和语言环境获得一个日历。
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //时间加减
        calendar.add(dateType, offsetValue);
        return calendar.getTime();
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern){
        if(date == null){
            return "";
        }
        //通过格式化输出日期
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date formatDate(String dateString, String pattern){
        //通过格式化输出日期
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            logger.error("[dateUtils|formatDate]日期转换失败。dateString={}", dateString, e);
            date = null;
        }
        return date;
    }

    /**
     * 获取指定日期是星期几
     * @param date
     * @return
     */
    public static String printWeekValue(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0){
            week = 0;
        }
        return weekDays[week];
    }

    public static int getAgeByBirthday(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }
}
