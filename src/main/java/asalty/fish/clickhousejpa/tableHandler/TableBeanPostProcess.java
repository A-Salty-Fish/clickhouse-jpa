package asalty.fish.clickhousejpa.tableHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseEntity;
import asalty.fish.clickhousejpa.exception.TableCreateException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import asalty.fish.clickhousejpa.util.ClassScanUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: 容器启动后对clickhouse表的处理
 * @date 2022/2/28 21:01
 */
@Service
public class TableBeanPostProcess {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    TableCreateHandler tableCreateHandler;

    @PostConstruct
    public void tableHandle() throws TypeNotSupportException, TableCreateException {
        List<Class<?>> entities = ClassScanUtil.getAllClassesWithAnnotation(applicationContext, ClickHouseEntity.class);
        for (Class<?> entity : entities) {
            tableCreateHandler.createTable(entity);
        }
    }
}
