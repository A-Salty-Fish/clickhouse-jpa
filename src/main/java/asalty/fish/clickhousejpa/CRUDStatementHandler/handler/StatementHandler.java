package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import asalty.fish.clickhousejpa.util.CacheUtil;
import asalty.fish.clickhousejpa.util.MethodParserUtil;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: 语句拦截器接口
 * @date 2022/2/28 19:32
 */

public interface StatementHandler {
    /**
     * 判断某方法是否需要该类型的拦截器被拦截
     * @param method
     * @return
     */
    public boolean needHandle(Method method);

    /**
     * 生成原始sql
     * @param method
     * @param args
     * @param entity
     * @return
     * @throws Exception
     */
    public String getRowStatement(Method method, Object[] args, Class<?> entity) throws Exception;

    /**
     * 缓存原始SQL，提高实时执行效率
     * @param method
     * @param args
     * @param entity
     * @return
     * @throws Exception
     */
    public default String getCacheStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        ConcurrentHashMap<Method, String> sqls = CacheUtil.daoRowSqls.computeIfAbsent(entity, k -> new ConcurrentHashMap<>());
        if (!sqls.containsKey(method)) {
            // 这里就懒得优化了，其实还可以优化一下
            String rowSql = getRowStatement(method, args, entity);
            sqls.put(method, rowSql);
            return rowSql;
        } else {
            return sqls.get(method);
        }
    }
    /**
     * 返回拦截后的sql
     * @param method
     * @return
     */
    public default String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return MethodParserUtil.prepareSqlArgs(getCacheStatement(method, args, entity), args, method);
    }

    /**
     * 拦截后的sql的结果处理器
     */
    public Object resultHandler(String sql, Class<?> entity, Method method) throws Exception;
}
