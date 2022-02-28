package asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler;

import net.sf.cglib.proxy.Enhancer;


/**
 * dao 动态代理接口
 * @author 13090
 */
public interface StatementProxy {

    StatementInterceptor statementInterceptor();

    @SuppressWarnings("unchecked")
    public default <T> T getProxy(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(statementInterceptor());
        return (T) enhancer.create();
    }
}
