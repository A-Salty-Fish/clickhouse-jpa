package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.annotation.ClickHouseColumn;
import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/1 17:45
 */
@Service
public class InsertStatementHandler implements StatementHandler {

    @Override
    public boolean needHandle(Method method) {
        return "create".equals(method.getName());
    }

    public String getTableName(Class<?> entity) {
        if (entity.isAnnotationPresent(ClickHouseTable.class) && !"".equals(entity.getAnnotation(ClickHouseTable.class).name())) {
            return entity.getAnnotation(ClickHouseTable.class).name();
        } else {
            return entity.getSimpleName();
        }
    }

    public String getColumnName(Field field) {
        if (field.isAnnotationPresent(ClickHouseColumn.class) && !"".equals(field.getAnnotation(ClickHouseColumn.class).name())) {
            return field.getAnnotation(ClickHouseColumn.class).name();
        } else {
            return field.getName();
        }
    }

    /**
     * 获取插入实体的sql
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public String getInsertSql(Class<?> entity, Object target) throws Exception {
        String tableName = getTableName(entity);
        StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append("( ");
        for (Field field : entity.getDeclaredFields()) {
            String columnName = getColumnName(field);
            insertSql.append(columnName).append(", ");
        }
        insertSql.delete(insertSql.length() - 2, insertSql.length());
        insertSql.append(") FORMAT Values (");
        for (Field field : entity.getDeclaredFields()) {
            String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            try {
                String value;
                // todo 类型转换
                if (field.getType().equals(String.class)) {
                    value = "'" + field.get(target).toString() + "'";
                } else if (field.getType().equals(Boolean.class)) {
                    value = "" + ((Boolean) field.get(target) ? "1" : "0");
                } else {
                    value = field.get(target).toString();
                }
                insertSql.append(value).append(", ");
            } catch (Exception e) {

            }
        }
        insertSql.delete(insertSql.length() - 2, insertSql.length());
        insertSql.append(")");
        return insertSql.toString();
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return getInsertSql(entity, args[0]);
    }

    @Override
    public Object resultHandler(String sql, Class<?> entity) throws Exception {
        return null;
    }
}
