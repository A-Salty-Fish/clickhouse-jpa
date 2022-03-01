package asalty.fish.clickhousejpa.util;

/**
 * @author 13090
 * @version 1.0
 * @description: todo 防止sql注入的工具类
 * @date 2022/3/1 14:19
 */

public class AntiSqlInjection {

    /**
     * 过滤SQL的输入参数
     * @param args
     * @return
     */
    public static String[] prepareSqlArgs(String[] args) {
        return null;
    }

    /**
     * 处理转义字符
     * @param args
     * @return
     */
    public static String[] prepareEscapeArgs(String[] args) {
        return null;
    }

    /**
     * 处理bool表达式注入
     * @param args
     * @return
     */
    public static String[] prepareBooleanArgs(String[] args) {
        return null;
    }

    /**
     * 处理函数注入
     * @param args
     * @return
     */
    public static String[] prepareFunctionArgs(String[] args) {
        return null;
    }
}
