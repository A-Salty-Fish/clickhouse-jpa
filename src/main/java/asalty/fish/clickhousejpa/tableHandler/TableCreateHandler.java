package asalty.fish.clickhousejpa.tableHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseColumn;
import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import asalty.fish.clickhousejpa.annotation.ClickHouseTimeColumns;
import asalty.fish.clickhousejpa.exception.TableCreateException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import asalty.fish.clickhousejpa.util.AnnotationUtil;
import asalty.fish.clickhousejpa.util.ClickhouseTypeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: 用于处理clickhouse表的创建
 * @date 2022/2/28 21:02
 */
@Service
@Slf4j
public class TableCreateHandler {

    @Value("${spring.jpa.clickhouse.table-update}")
    private Boolean createTable;

    @Resource
    Statement clickHouseStatement;

    /**
     * 获取建表sql
     * @param entity
     * @return
     */
    public String getCreateTableSql(Class<?> entity) throws TableCreateException, TypeNotSupportException {
        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE " + AnnotationUtil.getTableName(entity) + "(");
        // 取得主键字段
        List<Field> primaryKeyFields = new ArrayList<>();
        // 取得orderby字段
        List<Field> orderByFields = new ArrayList<>();
        boolean hasPrimaryKey = false;
        // 处理表字段
        for (Field field : entity.getDeclaredFields()) {
            ClickHouseColumn clickHouseColumn = field.getAnnotation(ClickHouseColumn.class);
            createTableSql.append(" " + AnnotationUtil.getColumnName(field) + " " +
                    ClickhouseTypeMap.getClickhouseType(field.getType().getSimpleName()) +
//                    ("".equals(getColumnComment(field)) ? " " : (" comment '" + getColumnComment(field)) + "'") +
                     ",");
            // 寻找主键和orderby字段
            if (clickHouseColumn != null && clickHouseColumn.isPrimaryKey()) {
                primaryKeyFields.add(field);
            }
            if (clickHouseColumn != null && clickHouseColumn.isOrderBy()) {
                orderByFields.add(field);
            }
        }
        // 处理自动生成列
        createTableSql.append(getAutoColumnsPartSql(entity));
        createTableSql.deleteCharAt(createTableSql.length() - 1);
        createTableSql.append(")");
        // 表引擎
        String engine = String.valueOf(entity.getAnnotation(ClickHouseTable.class).engine());
        createTableSql.append(" ENGINE = " + engine + "() ");
        // 表主键
        if (primaryKeyFields.size() > 0) {
            if (primaryKeyFields.size() > 1) {
                throw new TableCreateException("主键字段不能超过1个");
            }
            createTableSql.append(" PRIMARY KEY ");
            for (Field field : primaryKeyFields) {
                createTableSql.append(AnnotationUtil.getColumnName(field) + " ");
            }
        } else {
            throw new TableCreateException("没有主键字段");
        }
        // todo orderBy 暂时只支持order by 主键
        createTableSql.append(" ORDER BY " + AnnotationUtil.getColumnName(primaryKeyFields.get(0)));
        return createTableSql.toString();
    }

    /**
     * 判定是否需要创建表
     * @param entity
     * @return
     */
    public boolean needCreateTable(Class<?> entity){
        if (!createTable) {
            return false;
        }
        String testSql = "select * from " + AnnotationUtil.getTableName(entity) + " limit 1";
        try {
            ResultSet resultSet =clickHouseStatement.executeQuery(testSql);
        } catch (SQLException e) {
//            e.printStackTrace();
            if (e.getMessage().startsWith("DB::ExceptionDB::Exception: Table")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 建表实际逻辑
     * @param entity
     * @return
     */
    public void createTable(Class<?> entity) throws TypeNotSupportException, TableCreateException {
        if (needCreateTable(entity)) {
            String createTableSql = getCreateTableSql(entity);
            log.info(AnnotationUtil.getTableName(entity) + " createTableSql: " + createTableSql);
            try {
                clickHouseStatement.executeQuery(createTableSql);
                log.info(AnnotationUtil.getTableName(entity) + "create success");
            } catch (SQLException e) {
                log.info(AnnotationUtil.getTableName(entity) + "create failed");
                e.printStackTrace();
            }
        } else {
            log.info(AnnotationUtil.getTableName(entity) + " already exists.");
        }
    }

    final static String YEAR_COLUMN_NAME = "Year";

    final static String MONTH_COLUMN_NAME = "Month";

    final static String DAY_COLUMN_NAME = "Day";

    public String getAutoColumnsPartSql(Class<?> entity) throws TypeNotSupportException {
        ClickHouseTimeColumns clickHouseTimeColumns = entity.getAnnotation(ClickHouseTimeColumns.class);
        if (clickHouseTimeColumns == null) {
            StringBuilder autoColumnsPartSql = new StringBuilder();
            if (clickHouseTimeColumns.year()) {
                autoColumnsPartSql.append(" " + YEAR_COLUMN_NAME + " " + ClickhouseTypeMap.getClickhouseType(Integer.class.getSimpleName()) + ",");
            }
            if (clickHouseTimeColumns.month()) {
                autoColumnsPartSql.append(" " + MONTH_COLUMN_NAME + " " + ClickhouseTypeMap.getClickhouseType(Integer.class.getSimpleName()) + ",");
            }
            if (clickHouseTimeColumns.day()) {
                autoColumnsPartSql.append(" " + DAY_COLUMN_NAME + " " + ClickhouseTypeMap.getClickhouseType(Integer.class.getSimpleName()) + ",");
            }
        }
        return "";
    }
}
