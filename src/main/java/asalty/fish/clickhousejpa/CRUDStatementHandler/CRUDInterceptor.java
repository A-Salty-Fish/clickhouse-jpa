package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.StatementHandler;
import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 20:30
 */
@Slf4j
@Service
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
                    log.info(sql);
                    return statementHandler.resultHandler(sql, clickHouseRepository.entity(), method);
                }
                // 保证不会出现重复代理拦截器的情况
                break;
            }
        }
        return methodProxy.invokeSuper(obj, args);
    }
}
