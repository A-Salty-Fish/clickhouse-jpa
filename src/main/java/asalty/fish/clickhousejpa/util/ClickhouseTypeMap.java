package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.exception.TypeNotSupportException;

import java.util.HashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/1 13:05
 */

public class ClickhouseTypeMap {

    public static HashMap<String, String> javaTypeToClickhouseMap = new HashMap<String, String>();
    public static HashMap<String, String> clickhouseTypeToJavaMap = new HashMap<String, String>();

    /**
     * 初始化类型映射
     */
    static {
        javaTypeToClickhouseMap.put("Long", "UInt64");
        javaTypeToClickhouseMap.put("Integer", "UInt32");
        javaTypeToClickhouseMap.put("Boolean", "UInt8");
        javaTypeToClickhouseMap.put("String", "String");
        javaTypeToClickhouseMap.put("Date", "DateTime");
        javaTypeToClickhouseMap.put("Double", "Float64");

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
}
