package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import asalty.fish.clickhousejpa.util.AnnotationUtil;
import asalty.fish.clickhousejpa.util.ClickhouseTypeMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: 插入语句处理器
 * @date 2022/3/1 17:45
 */
@Service
public class InsertStatementHandler implements StatementHandler {

    @Override
    public boolean needHandle(Method method) {
        return "create".equals(method.getName());
    }

    @Resource
    Statement clickHouseStatement;

    @Resource
    ClickHouseMapper clickHouseMapper;

    /**
     * 获取插入实体的sql
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public String getInsertSql(Class<?> entity, Object target) throws Exception {
        String tableName = AnnotationUtil.getTableName(entity);
        StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append("( ");
        for (Field field : entity.getDeclaredFields()) {
            String columnName = AnnotationUtil.getColumnName(field);
            insertSql.append(columnName).append(", ");
        }
        insertSql.delete(insertSql.length() - 2, insertSql.length());
        insertSql.append(") FORMAT Values (");
        for (Field field : entity.getDeclaredFields()) {
            String value = ClickhouseTypeMap.convertTypeToString(target, field);
            insertSql.append(value).append(", ");
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
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception {
        try {
            clickHouseStatement.executeQuery(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
