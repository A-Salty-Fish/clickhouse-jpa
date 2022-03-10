package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.exception.IllegalSqlArguementException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/2 17:28
 */

public class MethodParserUtil {

    /**
     * 存放clickhouse仅含一个参数的函数
     */
    static HashMap<String, String> oneArgFunctions = new HashMap<>();

    static {
        oneArgFunctions.put("StartsWith", "startsWith");
    }

    public static String getConditionalSql(String methodNameLeft) {
        StringBuilder sql = new StringBuilder();
        if (methodNameLeft.length() > 0) {
            sql.append(" where ");
        }
        String[] methodNameLeftSplit = methodNameLeft.split("And|Or");
        for (String methodNameLeftSplitItem : methodNameLeftSplit) {
            String functionName = getOneArgFunction(methodNameLeftSplitItem);
            if (functionName != null) {
                sql.append(oneArgFunctions.get(functionName));
                sql.append("(");
                sql.append(methodNameLeftSplitItem.substring(functionName.length()));
                sql.append(", ");
                sql.append("? )");
            } else {
                sql.append(methodNameLeftSplitItem + " = ?");
                methodNameLeft = methodNameLeft.substring(methodNameLeftSplitItem.length());
                if (methodNameLeft.startsWith("And")) {
                    sql.append(" and ");
                    methodNameLeft = methodNameLeft.substring(3);
                } else if (methodNameLeft.startsWith("Or")) {
                    sql.append(" or ");
                    methodNameLeft = methodNameLeft.substring(2);
                }
            }
        }
        return sql.toString();
    }

    public static String prepareSqlArgs(String rowSql, Object[] args, Method method) throws TypeNotSupportException, IllegalSqlArguementException {
        StringBuilder sql = new StringBuilder(rowSql);
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            String arg = ClickhouseTypeMap.convertTypeToString(args[i], parameterTypes[i]);
            if (arg.contains("?")) {
                throw new IllegalSqlArguementException("arg cannot contain '?', please check.");
            }
            sql.replace(sql.indexOf("?"), sql.indexOf("?") + 1, arg);
        }
        return sql.toString();
    }

    private static String getOneArgFunction(String methodName) {
        return oneArgFunctions.keySet().stream().filter(methodName::startsWith).findFirst().orElse(null);
    }
}
