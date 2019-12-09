package com.isxcode.ispring.utils;

import com.isxcode.ispring.exception.IsxcodeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 *
 * @author ispong
 * @version v0.1.0
 * @date 2019-11-05
 */
public class DateUtils {

    /**
     * LocalDateTime 当前时间
     */
    private static LocalDateTime now = LocalDateTime.now();

    /**
     * LocalDateTime formatter yyyy-MM-dd HH:mm:ss
     */
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * LocalDateTime formatter yyyy-MM-dd
     */
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日历对象
     */
    private static Calendar calendar = Calendar.getInstance();

    /**
     * Date SimpleDateFormat yyyy-MM-dd HH:mm:ss
     */
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Date SimpleDateFormat yyyy-MM-dd
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 判断时间是否为当前天
     *
     * @param date 当前时间
     * @return 返回true为是  返回false为不是
     * @since 2019-11-05
     */
    public static Boolean isToday(LocalDateTime date) {

        return dateFormatter.format(date).equals(dateFormatter.format(now));
    }


    /**
     * 获取某月某周某一天
     *
     * @param date       输入的当前时间
     * @param monthIndex 第几月
     * @param weekIndex  第几周
     * @param dayIndex   第几天 （周日为0 周一为1 周二为2 依次叠加）
     * @return 具体时间
     * @since 2019-11-05
     */
    public static LocalDateTime getWeekDate(LocalDateTime date, int monthIndex, int weekIndex, int dayIndex) {

        return date.plusMonths(monthIndex)
                .plusWeeks(weekIndex)
                .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
                .plusDays(dayIndex);
    }

    /**
     * 获取上周日的23:59:59
     *
     * @param nowDate 当前时间
     * @param timeStr 时分秒Str
     * @return 上周日的23:59:59
     * @since 2019-12-09
     */
    public static Date getLastWeekEnd(Date nowDate, String timeStr) {

        calendar.setTime(nowDate);
        int dateIndex = calendar.get(Calendar.DAY_OF_WEEK);
        // 周日是 1
        if (dateIndex == 1) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1 - dateIndex);
        }
        // 拼接并转换date类型
        try {
            return dateTimeFormat.parse(dateFormat.format(calendar.getTime()) + " " + timeStr);
        } catch (ParseException e) {
            throw new IsxcodeException("时间格式异常");
        }
    }

    /**
     * 解析date Str转成Date类型
     *
     * @param dateStr date类型的Str yyyy-MM-dd Hh:mm:ss
     * @return 日期
     * @since 2019-12-09
     */
    public static Date parseDateTimeStrToDate(String dateStr) {

        try {
            return dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IsxcodeException("时间格式错误");
        }
    }

}
