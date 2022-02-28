package asalty.fish.clickhousejpa.CRUDStatementHandler;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 18:21
 */
@Service
public class FindAllProxy {
    @Resource
    FindAllInterceptor findAllInterceptor;

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(findAllInterceptor);
        return (T) enhancer.create();
    }
}
