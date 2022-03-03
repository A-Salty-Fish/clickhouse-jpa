package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.jdbc.ClickHouseJdbcConfig;
import asalty.fish.clickhousejpa.util.AnnotationUtil;
import asalty.fish.clickhousejpa.util.ClickhouseTypeMap;
import asalty.fish.clickhousejpa.util.MethodParserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

/**
 * @author 13090
 * @version 1.0
 * @description: 统计信息语句处理器
 * @date 2022/3/2 17:12
 */
@Service
public class StatisticsStatementHandler implements StatementHandler{

    static HashSet<String> statisticsMethods = new HashSet<>();

    @Resource
    ClickHouseJdbcConfig clickHouseJdbcConfig;

    static {
        statisticsMethods.add("count");
        statisticsMethods.add("max");
        statisticsMethods.add("min");
        statisticsMethods.add("avg");
        statisticsMethods.add("sum");
    }

    @Override
    public boolean needHandle(Method method) {
        // 是否匹配统计前缀
        return statisticsMethods.stream().anyMatch(sm ->method.getName().startsWith(sm));
    }

    @Override
    public String getRowStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return null;
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        String tableName = AnnotationUtil.getTableName(entity);
        StringBuilder methodName = new StringBuilder(method.getName());
        StringBuilder sql = new StringBuilder();
        for (String sm : statisticsMethods) {
            if (method.getName().startsWith(sm)) {
                sql = new StringBuilder("SELECT " + sm);
                methodName.delete(0, sm.length());
                int indexBy = methodName.indexOf("By");
                String columnName;
                if (indexBy > 0) {
                    columnName = methodName.substring(0, indexBy);
                    sql.append("(").append(columnName).append(")").append(" FROM ").append(tableName);
                    methodName.delete(0, indexBy + 2);
                    sql.append(MethodParserUtil.getConditionalSql(methodName.toString()));
                } else {
                    columnName = methodName.toString();
                    sql.append("(").append(columnName).append(")").append(" FROM ").append(tableName);
                }
            }
        }
        return MethodParserUtil.prepareSqlArgs(sql.toString(), args, method);
    }

    @Override
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception {
        ResultSet resultSet = clickHouseJdbcConfig.threadLocalStatement().executeQuery(sql);
        resultSet.next();
        Class<?> returnType = method.getReturnType();
        return ClickhouseTypeMap.convertStringToOtherType(returnType, resultSet.getString(1));
    }
}
