package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.StatementHandler;
import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: 负责将容器中的dao bean全部替换为dao bean的代理对象
 * @date 2022/2/28 18:53
 */
@Component
public class DaoBeanPostProcess implements ApplicationContextAware, BeanPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    StatementHandler[] statementHandlers;

    @Resource
    CRUDProxy crudProxy;

    // todo 后处理器接口化
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (statementHandlers == null) {
            String[] statementHandlerNames = applicationContext.getBeanNamesForType(StatementHandler.class);
            statementHandlers = new StatementHandler[statementHandlerNames.length];
            for (int i = 0; i < statementHandlerNames.length; i++) {
                statementHandlers[i] = applicationContext.getBean(statementHandlerNames[i], StatementHandler.class);
            }
            crudProxy.setStatementInterceptor(statementHandlers);
        }
        // 只替换dao bean
        if (bean.getClass().getAnnotation(ClickHouseRepository.class) != null) {
            return crudProxy.getProxy(bean.getClass());
        }
        return bean;
    }
}
