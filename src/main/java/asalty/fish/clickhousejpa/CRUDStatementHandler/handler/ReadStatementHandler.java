package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import asalty.fish.clickhousejpa.jdbc.ClickHouseJdbcConfig;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import asalty.fish.clickhousejpa.util.MethodParserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: 用于搜索的语句处理器
 * @date 2022/2/28 16:44
 */
@Service
public class ReadStatementHandler implements StatementHandler {

    String baseSQL = "select * from";

    @Value("${spring.jpa.clickhouse.database}")
    String database;

    @Resource
    ClickHouseJdbcConfig clickHouseJdbcConfig;

    @Resource
    ClickHouseMapper clickHouseMapper;

    public String databaseSQL(Class<?> clazz) {
        ClickHouseTable clickHouseTable = clazz.getAnnotation(ClickHouseTable.class);
        String tableName;
        if (clickHouseTable != null) {
            tableName = clickHouseTable.name();
        } else {
            tableName = clazz.getSimpleName();
        }
        return baseSQL + " " + database + "." + tableName;
    }

    public String limitSQL(Class<?> clazz) {
        return databaseSQL(clazz) + " limit " + "?";
    }

    public <T> List<T> findAll(Class<T> clazz) throws Exception {
        String sql = databaseSQL(clazz);
        return clickHouseMapper.convertResultSetToList(clickHouseJdbcConfig.threadLocalStatement().executeQuery(sql), clazz);
    }

    public <T> List<T> findAllLimitedBy(Class<T> clazz, int size) throws Exception {
        String sql = limitSQL(clazz);
        sql = sql.replace("?", String.valueOf(size));
        return clickHouseMapper.convertResultSetToList(clickHouseJdbcConfig.threadLocalStatement().executeQuery(sql), clazz);
    }

    /**
     * 暂时只支持 and 和 or 懒得写其他的解析了
     *
     * @param clazz
     * @param methodName
     * @return
     * @throws Exception
     */
    public String getFindAllSqlFromMethodName(Class<?> clazz, String methodName) throws Exception {
        // 方法前缀
        if (!methodName.startsWith("findAllBy")) {
            return null;
        }
        String methodNameLeft = methodName.substring(9);
        StringBuilder sql = new StringBuilder(databaseSQL(clazz));
        sql.append(MethodParserUtil.getConditionalSql(methodNameLeft));
        return sql.toString();
    }


    @Override
    public boolean needHandle(Method method) {
        return method.getName().startsWith("findAllBy");
    }

    @Override
    public String getRowStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return null;
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        StringBuilder sql = new StringBuilder(getFindAllSqlFromMethodName(entity, method.getName()));
        return MethodParserUtil.prepareSqlArgs(sql.toString(), args, method);
    }

    @Override
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception {
        return clickHouseMapper.convertResultSetToList(clickHouseJdbcConfig.threadLocalStatement().executeQuery(sql), entity);
    }
}
