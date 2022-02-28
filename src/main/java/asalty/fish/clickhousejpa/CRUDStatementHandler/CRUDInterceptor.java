package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 20:30
 */
public class CRUDInterceptor implements MethodInterceptor{

    private StatementHandler[] statementHandlers;

    public void setStatementInterceptor(StatementHandler[] statementHandlers){
        this.statementHandlers = statementHandlers;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        for (StatementHandler statementHandler : statementHandlers) {
            if (statementHandler.needHandle(method)) {
                ClickHouseRepository clickHouseRepository = method.getDeclaringClass().getAnnotation(ClickHouseRepository.class);
                if (clickHouseRepository != null) {
                    String sql = statementHandler.getStatement(method, args, clickHouseRepository.entity());
                    return statementHandler.resultHandler(sql, clickHouseRepository.entity());
                }
            }
        }
        return methodProxy.invokeSuper(obj, args);
    }
}
