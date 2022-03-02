package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/2 17:28
 */

public class MethodParserUtil {

    public static String getConditionalSql(String methodNameLeft) {
        StringBuilder sql = new StringBuilder();
        if (methodNameLeft.length() > 0) {
            sql.append(" where ");
        }
        String[] methodNameLeftSplit = methodNameLeft.split("And|Or");
        for (String methodNameLeftSplitItem : methodNameLeftSplit) {
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
        return sql.toString();
    }

    public static String prepareSqlArgs(String rowSql, Object[] args) {
        StringBuilder sql = new StringBuilder(rowSql);
        for (Object arg : args) {
            sql.replace(sql.indexOf("?"), sql.indexOf("?") + 1, arg.toString());
        }
        return sql.toString();
    }
}
