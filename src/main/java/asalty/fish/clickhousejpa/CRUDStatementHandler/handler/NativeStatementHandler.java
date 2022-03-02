package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.annotation.ClickHouseNativeQuery;
import asalty.fish.clickhousejpa.exception.NativeQueryException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: 手写SQl语句处理器
 * @date 2022/3/2 15:07
 */
@Service
public class NativeStatementHandler implements StatementHandler {

    @Resource
    Statement clickHouseStatement;

    public String getNativeSql(Method method, Object[] args) throws NativeQueryException {
        ClickHouseNativeQuery annotation = method.getAnnotation(ClickHouseNativeQuery.class);
        StringBuilder rowSql = new StringBuilder(annotation.value());
        if (rowSql.length() == 0) {
            throw new NativeQueryException("native sql is invalid");
        }
        // todo 注入问题
        int argsCount = (int) rowSql.chars().filter(ch -> ch == '?').count();
        if (argsCount != args.length) {
            throw new NativeQueryException("native sql args count is invalid, expect " + argsCount + " but " + args.length);
        }
        // todo 类型问题
        for (int i = 0; i < args.length; i++) {
            rowSql.replace(rowSql.indexOf("?"), rowSql.indexOf("?") + 1, args[i].toString());
        }
        // todo 参数名映射
        return rowSql.toString();
    }

    @Override
    public boolean needHandle(Method method) {
        return method.isAnnotationPresent(ClickHouseNativeQuery.class);
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return getNativeSql(method, args);
    }

    @Override
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception {
        Class<?> returnType = method.getReturnType();
        ResultSet resultSet = clickHouseStatement.executeQuery(sql);
        Object result;
        if (returnType == void.class) {
            return null;
        } else {
            if (returnType.equals(Long.class)) {
                result = resultSet.next() ? resultSet.getLong(1) : 0L;
            } else {
                throw new TypeNotSupportException("return type" + returnType.getSimpleName() + " is not support");
            }
        }
        return result;
    }
}
