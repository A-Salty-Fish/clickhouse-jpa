package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.annotation.ClickHouseColumn;
import asalty.fish.clickhousejpa.annotation.ClickHouseTable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 13090
 * @version 1.0
 * @description: 项目注解工具类
 * @date 2022/3/2 10:35
 */

public class AnnotationUtil {

    public static String getColumnName(Field field) {
        String columnName = field.getName();
        ClickHouseColumn clickHouseColumn = field.getAnnotation(ClickHouseColumn.class);
        if (clickHouseColumn != null && !"".equals(clickHouseColumn.name())) {
            columnName = clickHouseColumn.name();
        }
        return columnName;
    }

    public static String getTableName(Class<?> entity) {
        if (entity.isAnnotationPresent(ClickHouseTable.class) && !"".equals(entity.getAnnotation(ClickHouseTable.class).name())) {
            return entity.getAnnotation(ClickHouseTable.class).name();
        } else {
            return entity.getSimpleName();
        }
    }
}
