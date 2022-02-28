package asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: cglib代理接口
 * @date 2022/2/28 19:35
 */

public interface StatementInterceptor extends MethodInterceptor {

    public StatementHandler statementHandler();

    public Object resultHandler(String sql, Class<?> entity) throws Exception;

    @Override
    public default Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (statementHandler().needHandle(method)) {
            ClickHouseRepository clickHouseRepository = method.getDeclaringClass().getAnnotation(ClickHouseRepository.class);
            if (clickHouseRepository != null) {
                String sql = statementHandler().getStatement(method, args, clickHouseRepository.entity());
                return resultHandler(sql, clickHouseRepository.entity());
            }
        }
        return methodProxy.invokeSuper(obj, args);
    }

}
