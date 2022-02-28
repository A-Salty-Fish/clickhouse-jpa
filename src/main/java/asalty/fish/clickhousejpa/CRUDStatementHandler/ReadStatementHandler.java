package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: 用于搜索的语句处理器
 * @date 2022/2/28 16:44
 */
@Configuration
public class ReadStatementHandler {

    String baseSQL = "select * from";

    @Value("${spring.jpa.clickhouse.database}")
    String database;

    @Resource
    Statement clickHouseStatement;

    @Resource
    ClickHouseMapper clickHouseMapper;

    public String databaseSQL(Class<?> clazz) {
        ClickHouseTable clickHouseTable = clazz.getAnnotation(ClickHouseTable.class);
        String tableName;
        if (clickHouseTable != null) {
            tableName = clickHouseTable.name();
        } else {
            tableName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        }
        return baseSQL + " " + database + "." + tableName;
    }

    public String limitSQL(Class<?> clazz) {
        return databaseSQL(clazz) + " limit " + "?";
    }

    public <T> List<T> findAll(Class<T> clazz) throws SQLException {
        String sql = databaseSQL(clazz);
        return clickHouseMapper.convertResultSetToList(clickHouseStatement.executeQuery(sql), clazz);
    }

    public <T> List<T> findAllLimitedBy(Class<T> clazz, int size) throws SQLException {
        String sql = limitSQL(clazz);
        sql = sql.replace("?", String.valueOf(size));
        return clickHouseMapper.convertResultSetToList(clickHouseStatement.executeQuery(sql), clazz);
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

    public String prepareFindAllSQL(Class<?> clazz, String methodName, String[] args) throws Exception {
        StringBuilder sql = new StringBuilder(getFindAllSqlFromMethodName(clazz, methodName));
        for (String arg : args) {
            sql.replace(sql.indexOf("?"), sql.indexOf("?") + 1, arg);
        }
        return sql.toString();
    }
}
