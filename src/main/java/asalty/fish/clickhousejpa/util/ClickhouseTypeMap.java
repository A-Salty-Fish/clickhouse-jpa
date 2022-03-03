package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.exception.TypeNotSupportException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: 类型映射库 主要用于获取类型的映射
 * todo 以后再重构
 * @date 2022/3/1 13:05
 */

public class ClickhouseTypeMap {

    public static HashMap<String, String> javaTypeToClickhouseMap = new HashMap<String, String>();
    public static HashMap<String, String> clickhouseTypeToJavaMap = new HashMap<String, String>();

    /**
     * 初始化类型映射
     */
    static {
        javaTypeToClickhouseMap.put(Long.class.getSimpleName(), "UInt64");
        javaTypeToClickhouseMap.put(Integer.class.getSimpleName(), "UInt32");
        javaTypeToClickhouseMap.put(Boolean.class.getSimpleName(), "UInt8");
        javaTypeToClickhouseMap.put(String.class.getSimpleName(), "String");
        javaTypeToClickhouseMap.put(LocalDateTime.class.getSimpleName(), "DateTime");
        javaTypeToClickhouseMap.put(LocalDate.class.getSimpleName(), "Date");
        javaTypeToClickhouseMap.put(Double.class.getSimpleName(), "Float64");

        javaTypeToClickhouseMap.forEach((k, v) -> {
            clickhouseTypeToJavaMap.put(v, k);
        });
    }

    public static String getClickhouseType(String javaType) throws TypeNotSupportException {
        String clickhouseType = javaTypeToClickhouseMap.get(javaType);
        if (clickhouseType == null) {
            throw new TypeNotSupportException("clickhouse type not support:" + javaType);
        }
        return javaTypeToClickhouseMap.get(javaType);
    }

    public static String getJavaType(String clickhouseType) throws TypeNotSupportException {
        if (clickhouseType == null) {
            throw new TypeNotSupportException("java type not support:" + clickhouseType);
        }
        return clickhouseTypeToJavaMap.get(clickhouseType);
    }

    /**
     * 将 clichouse 返回的 string 值转换为对应的类型并填入目标对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertStringToOtherType(Class<T> type, String value) throws TypeNotSupportException {
        if (type == String.class) {
            return (T) value;
        } else if (type == Long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Double.class) {
            return (T) Double.valueOf(value);
        } else if (type == LocalDateTime.class) {
            return (T) LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (type == LocalDate.class) {
            return (T) LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            throw new TypeNotSupportException("type not support:" + type.getSimpleName());
        }
    }

    public static void convertAndSetStringToOtherType(Object t, Class<?> type, Method method, String value) {
        try {
            if (type == String.class) {
                method.invoke(t, value);
            } else if (type == Long.class) {
                method.invoke(t, Long.parseLong(value));
            } else if (type == Boolean.class) {
                method.invoke(t, Boolean.parseBoolean(value));
            } else if (type == LocalDateTime.class) {
                // 去除时区信息
                if (value.indexOf("+") > 0) {
                    value = value.substring(0, value.indexOf("+"));
                }
                value = value.replace("T", " ");
                method.invoke(t, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else if (type == LocalDate.class) {
                method.invoke(t, LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else if (type == Integer.class) {
                method.invoke(t, Integer.parseInt(value));
            } else{
                throw new TypeNotSupportException("type not support: " + type.getSimpleName());
            }
            // todo 支持其他类型
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将java类型转换为clickhouse支持的字符串
     *
     * @param target
     * @param field
     * @return
     * @throws IllegalAccessException
     * @throws TypeNotSupportException
     */
    public static String convertTypeToString(Object target, Field field) throws IllegalAccessException, TypeNotSupportException {
        String value;
        if (field.getType().equals(String.class)) {
            value = "'" + field.get(target).toString() + "'";
        } else if (field.getType().equals(Boolean.class)) {
            value = "" + ((Boolean) field.get(target) ? "1" : "0");
        } else if (field.getType().equals(LocalDateTime.class)) {
            value = "'" + ((LocalDateTime) field.get(target)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
        } else if (field.getType().equals(LocalDate.class)) {
            value = "'" + ((LocalDate) field.get(target)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'";
        } else if (field.getType().equals(Long.class)) {
            value = "" + field.get(target);
        } else if (field.getType().equals(Double.class)) {
            value = "" + field.get(target);
        } else if (field.getType().equals(Integer.class)) {
            value = "" + field.get(target);
        } else {
            throw new TypeNotSupportException("type not support: " + field.getType().getSimpleName());
        }
        return value;
    }

    public static String convertTypeToString(Object target, Class<?> type) throws TypeNotSupportException {
        String value;
        if (type.equals(String.class)) {
            value = "'" + target.toString() + "'";
        } else if (type.equals(Boolean.class)) {
            value = "" + ((Boolean) target ? "1" : "0");
        } else if (type.equals(LocalDateTime.class)) {
            value = "'" + ((LocalDateTime) target).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
        } else if (type.equals(LocalDate.class)) {
            value = "'" + ((LocalDate) target).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'";
        } else if (type.equals(Long.class)) {
            value = "" + target;
        } else if (type.equals(Double.class)) {
            value = "" + target;
        } else if (type.equals(Integer.class)) {
            value = "" + target;
        } else {
            throw new TypeNotSupportException("type not support: " + type.getSimpleName());
        }
        return value;
    }
}
