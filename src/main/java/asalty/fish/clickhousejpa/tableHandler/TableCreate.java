package asalty.fish.clickhousejpa.tableHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: 用于处理clickhouse表的创建
 * @date 2022/2/28 21:02
 */
@Service
public class TableCreate {

    @Value("${spring.jpa.clickhouse.table-update}")
    private Boolean createTable;

    @Resource
    Statement clickHouseStatement;

    /**
     * 获取表名
     * @param entity
     * @return
     */
    public String getTableName(Class<?> entity) {
        ClickHouseTable clickHouseTable = entity.getAnnotation(ClickHouseTable.class);
        String tableName;
        if (clickHouseTable == null) {
            tableName = entity.getSimpleName();
        } else {
            tableName = clickHouseTable.name();
        }
        return tableName;
    }

    /**
     * 获取建表sql
     * @param entity
     * @return
     */
    public String getCreateTableSql(Class<?> entity) {
        return null;
    }

    /**
     * 建表实际逻辑
     * @param entity
     * @return
     */
    public String createTable(Class<?> entity) {
        return null;
    }
}
