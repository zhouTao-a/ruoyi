package org.dromara.mes.utils;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 农历与阳历互转工具类
 */
public class LunarSolarUtils {

    /**
     * 将农历 LocalDate 转换为对应的阳历 LocalDate，可能包含一个或两个结果（处理闰月）
     *
     * @param lunarDate 农历日期（不区分是否闰月）
     * @return 对应的阳历 LocalDate 列表（最多两个）
     */
    public static List<LocalDate> convertLunarToSolar(LocalDate lunarDate) {
        List<LocalDate> solarDates = new ArrayList<>();
        int year = lunarDate.getYear();
        int month = lunarDate.getMonthValue();
        int day = lunarDate.getDayOfMonth();

        // 1. 普通月
        try {
            Lunar lunar = Lunar.fromYmd(year, month, day);
            Solar solar = lunar.getSolar();
            solarDates.add(LocalDate.of(solar.getYear(), solar.getMonth(), solar.getDay()));
        } catch (Exception ignored) {}

        // 2. 闰月（如果存在）
        try {
            Lunar lunarLeap = Lunar.fromYmd(year, month, day);
            Solar solarLeap = lunarLeap.getSolar();
            LocalDate solarDate = LocalDate.of(solarLeap.getYear(), solarLeap.getMonth(), solarLeap.getDay());
            if (!solarDates.contains(solarDate)) {
                solarDates.add(solarDate);
            }
        } catch (Exception ignored) {}

        return solarDates;
    }

    /**
     * 将阳历 LocalDate 转换为农历 Lunar 对象信息（唯一）
     *
     * @param solarDate 阳历日期
     * @return 对应农历的字符串信息
     */
    public static String convertSolarToLunar(LocalDate solarDate) {
        Solar solar = Solar.fromYmd(solarDate.getYear(), solarDate.getMonthValue(), solarDate.getDayOfMonth());
        Lunar lunar = solar.getLunar();

        StringBuilder sb = new StringBuilder();
        sb.append("农历：").append(lunar.getYear()).append("年")
          .append(lunar.getMonth()).append("月")
          .append(lunar.getDay()).append("日");

        return sb.toString();
    }

    public static void main(String[] args) {
        // 农历转阳历（示例：2020年4月1日，可能为闰月）
        LocalDate lunarDate = LocalDate.of(2020, 4, 1);
        List<LocalDate> solarList = LunarSolarUtils.convertLunarToSolar(lunarDate);
        System.out.println("【农历转阳历】");
        for (LocalDate date : solarList) {
            System.out.println("阳历：" + date);
        }

        // 阳历转农历
        LocalDate solarDate = LocalDate.of(2020, 5, 23);
        String lunarInfo = LunarSolarUtils.convertSolarToLunar(solarDate);
        System.out.println("\n【阳历转农历】");
        System.out.println("阳历：" + solarDate + " -> " + lunarInfo);
    }
}
