package asalty.fish.clickhousejpa.CRUDStatementHandler;

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

    @Resource
    FindAllProxy findAllProxy;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 只替换dao bean
        if (bean.getClass().getAnnotation(ClickHouseRepository.class) != null) {
            // 如果遇到需要替换的Bean，我们直接换成自己实现的bean
            // 这里的myConfig要继承自defaultConfig，否则引用的地方会报错
            return findAllProxy.getProxy(bean.getClass());
        }
        return bean;
    }
}
