package org.dromara.mes.utils;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 农历 <-> 阳历 工具类，支持 Date（包含时分秒），基于 cn.6tail:lunar 1.7.4
 */
public class LunarSolarDateUtils {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

    /**
     * 农历转阳历（返回所有可能阳历 Date，保留时分秒）
     * @param lunarDate Date（表示农历，包含时分秒）
     * @return List<Date>（可能多个阳历结果，包含原时分秒）
     */
    public static List<Date> convertLunarToSolar(Date lunarDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lunarDate);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        List<Date> result = new ArrayList<>();

        List<Lunar> lunarList = getAllLunarDaysOfYear(year);
        for (Lunar lunar : lunarList) {
            if (lunar.getMonth() == month && lunar.getDay() == day) {
                Solar solar = lunar.getSolar();
                Calendar solarCal = Calendar.getInstance();
                solarCal.set(solar.getYear(), solar.getMonth() - 1, solar.getDay(), hour, minute, second);
                solarCal.set(Calendar.MILLISECOND, 0);
                result.add(solarCal.getTime());
            }
        }

        return result;
    }

    /**
     * 阳历转农历（唯一对应，保留时分秒）
     * @param solarDate Date（阳历）
     * @return Date（对应农历，保留时分秒）
     */
    public static Date convertSolarToLunar(Date solarDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(solarDate);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        Solar solar = Solar.fromYmdHms(year, month, day, hour, minute, second);
        Lunar lunar = solar.getLunar();

        Calendar lunarCal = Calendar.getInstance();
        lunarCal.set(lunar.getYear(), lunar.getMonth() - 1, lunar.getDay(), hour, minute, second);
        lunarCal.set(Calendar.MILLISECOND, 0);

        return lunarCal.getTime();
    }

    /**
     * 获取某年所有农历日期（用于判断农历对应的所有阳历）
     */
    private static List<Lunar> getAllLunarDaysOfYear(int year) {
        List<Lunar> list = new ArrayList<>();
        Lunar day = Lunar.fromYmd(year, 1, 1); // 农历正月初一
        while (day.getYear() == year) {
            list.add(day);
            day = day.next(1); // 向后一天
        }
        return list;
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    public static Date parseDate(String str) {
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException("解析日期失败：" + str, e);
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 农历 2020年四月初一 12:30:45
        Date lunarDate = sdf.parse("2025-06-10 12:30:45");
        List<Date> solarList = LunarSolarDateUtils.convertLunarToSolar(lunarDate);
        System.out.println("农历 → 阳历：");
        for (Date d : solarList) {
            System.out.println(LunarSolarDateUtils.formatDate(d));
        }

        // 阳历 2020-05-23 08:15:00
        Date solarDate = sdf.parse("2020-05-23 08:15:00");
        Date lunarResult = LunarSolarDateUtils.convertSolarToLunar(solarDate);
        System.out.println("阳历 → 农历：");
        System.out.println(LunarSolarDateUtils.formatDate(lunarResult));
    }
}
