package asalty.fish.clickhousejpa.util;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: 缓存工具类
 * @date 2022/3/3 15:11
 */

public class CacheUtil {

    /**
     * 用于缓存 dao 解析的原始sql
     * todo 以后换成 caffeine
     */
    public static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Method, String>> daoRowSqls = new ConcurrentHashMap<>();
}
