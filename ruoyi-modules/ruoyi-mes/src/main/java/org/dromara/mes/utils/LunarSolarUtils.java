package org.dromara.mes.utils;

import cn.hutool.core.date.ChineseDate;
import org.dromara.common.core.utils.DateUtils;
import java.util.*;

import static java.lang.System.*;

public class LunarSolarUtils {

   /**
     * 农历转公历
     */
    public static List<Date> lunarToSolar(Date lunarDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lunarDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        Set<String> strings = lunarToSolarTryBoth(year, month, day);
        List<Date> solarList = new ArrayList<>();
        strings.forEach(string -> solarList.add(DateUtils.parseDate(string + " " + hour + ":" + minute + ":" + second)));
        return solarList;
    }

    /**
     * 农历转公历
     */
    public static Set<String> lunarToSolarTryBoth(int year, int month, int day) {
        Set<String> list = new HashSet<>();
        // 尝试非闰月
        ChineseDate cd = new ChineseDate(year, month, day, false);
        Date date = cd.getGregorianDate();
        list.add(DateFormatUtils.formatDate(date));


        // 尝试闰月
        ChineseDate cdLeap = new ChineseDate(year, month, day, true);
        Date dateLeap = cdLeap.getGregorianDate();
        list.add(DateFormatUtils.formatDate(dateLeap));
        return list;
    }

    /**
     *  公历转农历
     */
    public static Date solarToLunar(Date lunarDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lunarDate);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        ChineseDate chineseDate = new ChineseDate(lunarDate);
        String stringNormal = chineseDate.toStringNormal();
        return DateUtils.parseDate(stringNormal + " " + hour + ":" + minute + ":" + second);
    }

    public static void main(String[] args) {
        Date date = new Date();
        List<Date> dates = lunarToSolar(date);
        out.println(DateFormatUtils.formatDateTime(solarToLunar(date)));
        out.println(DateFormatUtils.formatDateTime(date));
        for (Date d : dates) {
            out.println(DateFormatUtils.formatDateTime(d));
        }
    }

}
