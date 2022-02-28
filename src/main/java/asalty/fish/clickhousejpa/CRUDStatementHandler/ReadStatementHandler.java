package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import asalty.fish.clickhousejpa.jdbc.ClickHouseJdbcConfig;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
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
        return baseSQL + " " + tableName;
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

}
