package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.StatementHandler;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 20:29
 */
@Service
public class CRUDProxy{

    @Resource
    CRUDInterceptor crudInterceptor;

    public void setStatementInterceptor(StatementHandler[] statementHandlers){
        this.crudInterceptor.setStatementInterceptor(statementHandlers);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(crudInterceptor);
        return (T) enhancer.create();
    }
}
