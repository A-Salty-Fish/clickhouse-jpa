package asalty.fish.clickhousejpa.tableHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseEntity;
import asalty.fish.clickhousejpa.exception.TableCreateException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import asalty.fish.clickhousejpa.util.ClassScanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 13090
 * @version 1.0
 * @description: 容器启动后对clickhouse表的处理
 * @date 2022/2/28 21:01
 */
@Service
public class TableBeanPostProcess {
    @Autowired
    private ApplicationContext context;

    @javax.annotation.Resource
    TableCreateHandler tableCreateHandler;

    @PostConstruct
    public void tableHandle() throws TypeNotSupportException, TableCreateException {
        String[] springBootAppBeanName = context.getBeanNamesForAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        Object springbootApplication = context.getBean(springBootAppBeanName[0]);
        List<Class<?>> entities = ClassScanUtil.getClickHouseEntities(springbootApplication.getClass().getPackage().getName(), "/**/*.class");
        for (Class<?> entity : entities) {
            tableCreateHandler.createTable(entity);
        }
    }

}
