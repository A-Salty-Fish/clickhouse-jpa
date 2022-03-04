package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.jdbc.ClickHouseJdbcConfig;
import asalty.fish.clickhousejpa.util.AnnotationUtil;
import asalty.fish.clickhousejpa.util.ClickhouseTypeMap;
import asalty.fish.clickhousejpa.util.MethodParserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: 批量插入语句处理器
 * @date 2022/3/4 10:53
 */
@Service
public class BatchInsertStatementHandler implements StatementHandler {

    @Override
    public boolean needHandle(Method method) {
        return "batchCreate".equals(method.getName()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(List.class);
    }


    @Override
    public String getRowStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        String tableName = AnnotationUtil.getTableName(entity);
        StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append("( ");
        for (Field field : entity.getDeclaredFields()) {
            String columnName = AnnotationUtil.getColumnName(field);
            insertSql.append(columnName).append(", ");
        }
        insertSql.delete(insertSql.length() - 2, insertSql.length());
        insertSql.append(") FORMAT Values ");
        return insertSql.toString();
    }

    /**
     * 这里缓存一下每个实体公共的 (?,?...) 字符串部分
     */
    ConcurrentHashMap<Class<?>, String> rowValueSqlMap = new ConcurrentHashMap<>();

    public String getRowValueSql(Class<?> entity) throws Exception {
        if (rowValueSqlMap.containsKey(entity)) {
            return rowValueSqlMap.get(entity);
        }
        StringBuilder rowValueSql = new StringBuilder("(");
        for (int i = 0; i < entity.getDeclaredFields().length; i++) {
            rowValueSql.append("?,");
        }
        rowValueSql.deleteCharAt(rowValueSql.length() - 1);
        rowValueSql.append(")");
        rowValueSqlMap.put(entity, rowValueSql.toString());
        return rowValueSql.toString();
    }

    /**
     * 由于参数数量不同，这里只能缓存前部的公共 statement 和后面的公共填充参数部分
     * @param method
     * @param args
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        StringBuilder sql = new StringBuilder(getCacheStatement(method, args, entity));
        List list = (List) args[0];
        for (int i = 0; i < list.size(); i++) {
            // 拿到并填充 (?,?,?) 字符串部分
            StringBuilder valueSql = new StringBuilder(getRowValueSql(entity));
            Object o = list.get(i);
            for (Field field : o.getClass().getDeclaredFields()) {
                String value = ClickhouseTypeMap.convertTypeToString(o, field);
                valueSql.replace(sql.indexOf("?"), valueSql.indexOf("?") + 1, value);
            }
            sql.append(valueSql).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        return sql.toString();
    }


    @Resource
    ClickHouseJdbcConfig clickHouseJdbcConfig;

    @Override
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception {
        try {
            Statement statement = clickHouseJdbcConfig.threadLocalStatement();
            statement.executeQuery(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
