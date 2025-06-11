package org.dromara.mes.utils;

import org.dromara.common.sensitive.annotation.Sensitive;
import org.dromara.common.sensitive.core.SensitiveStrategy;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * 通用的脱敏工具类，用于处理标注了 @Sensitive 注解的字段
 */
public class SensitiveDataUtils {

    // 私有构造器，防止实例化
    private SensitiveDataUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 脱敏处理入口方法
     *
     * @param obj 可为对象或集合（如 List）
     * @return 脱敏后的数据（就地修改原对象）
     */
    @SuppressWarnings("unchecked")
    public static <T> T handle(T obj) {
        if (obj == null) return null;

        // 如果是集合类型，递归处理集合中的每一个元素
        if (obj instanceof Collection<?>) {
            return (T) ((Collection<?>) obj).stream()
                .map(SensitiveDataUtils::handle)
                .toList();
        }

        // 如果是 Map，默认跳过（你可以根据需要自行扩展）
        if (obj instanceof Map<?, ?>) {
            return obj;
        }

        // 普通对象，进行字段脱敏处理
        desensitizeFields(obj);
        return obj;
    }

    /**
     * 对传入对象执行字段级脱敏，处理所有声明字段（包括父类）
     *
     * @param obj 任意对象，字段可能带有 @Sensitive 注解
     */
    private static void desensitizeFields(Object obj) {
        Class<?> clazz = obj.getClass();

        // 遍历当前类及其父类，直到 Object 为止
        while (clazz != null && clazz != Object.class) {
            // 获取当前类声明的字段
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                // 将每个字段交给独立处理方法
                handleSensitiveField(field, obj);
            }

            // 递归处理父类
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 对某个字段进行脱敏处理，如果字段有 @Sensitive 注解且为 String 类型
     *
     * @param field 当前字段
     * @param obj   当前字段所属对象
     */
    private static void handleSensitiveField(Field field, Object obj) {
        // 获取 @Sensitive 注解，如果没有则直接跳过
        Sensitive annotation = field.getAnnotation(Sensitive.class);
        if (annotation == null) {
            return;
        }

        // 只处理 String 类型字段（其他类型跳过）
        if (field.getType() != String.class) {
            return;
        }

        try {
            // 允许访问 private 字段
            field.setAccessible(true);

            // 获取字段值
            String originalValue = (String) field.get(obj);

            // 值不为空才处理
            if (originalValue != null) {
                // 获取注解指定的脱敏策略
                SensitiveStrategy strategy = annotation.strategy();

                // 获取对应的脱敏处理函数
                Function<String, String> desensitizer = strategy.desensitizer();

                // 执行脱敏并写回字段
                String maskedValue = desensitizer.apply(originalValue);
                field.set(obj, maskedValue);
            }
        } catch (Exception ignored) {

        }
    }

}
