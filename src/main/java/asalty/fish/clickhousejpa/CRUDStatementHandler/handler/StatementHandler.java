package asalty.fish.clickhousejpa.CRUDStatementHandler.handler;

import java.lang.reflect.Method;

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
     * 返回拦截后的sql
     * @param method
     * @return
     */
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception;

    /**
     * 拦截后的sql的结果处理器
     */
    public Object resultHandler(String sql, Class<?> entity) throws Exception;
}
