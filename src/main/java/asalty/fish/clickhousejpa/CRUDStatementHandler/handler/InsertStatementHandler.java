package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.jdbc.ClickHouseJdbcConfig;
import asalty.fish.clickhousejpa.util.AnnotationUtil;
import asalty.fish.clickhousejpa.util.CacheUtil;
import asalty.fish.clickhousejpa.util.ClickhouseTypeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: 插入语句处理器
 * @date 2022/3/1 17:45
 */
@Service
@Slf4j
public class InsertStatementHandler implements StatementHandler {

    @Override
    public boolean needHandle(Method method) {
        return "create".equals(method.getName());
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
        insertSql.append(") FORMAT Values (");
        for (Field ignored : entity.getDeclaredFields()) {
            insertSql.append(" ? ").append(", ");
        }
        insertSql.delete(insertSql.length() - 2, insertSql.length());
        insertSql.append(")");
        return insertSql.toString();
    }

    /**
     * 获取插入实体的sql
     *
     * @return
     * @throws Exception
     */
    public String getInsertSql(String rowSql, Object target) throws Exception {
        StringBuilder sql = new StringBuilder(rowSql);
        for (Field field : target.getClass().getDeclaredFields()) {
            String value = ClickhouseTypeMap.convertTypeToString(target, field);
            sql.replace(sql.indexOf("?"), sql.indexOf("?") + 1, value);
        }
        return sql.toString();
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        // 这里的参数处理比较特殊，所以需要重写
        String rowSql = getRowStatement(method, args, entity);
        return getInsertSql(rowSql, args[0]);
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
