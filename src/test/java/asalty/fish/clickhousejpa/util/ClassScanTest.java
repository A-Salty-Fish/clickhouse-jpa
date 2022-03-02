package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.annotation.ClickHouseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/1 17:15
 */
@SpringBootTest
public class ClassScanTest {

    @Test
    public void testScan() throws Exception {
        List<Class<?>> classes = ClassScanUtil.getAllClassByPackageName(Package.getPackage("asalty.fish.clickhousejpa"));
        classes.forEach(System.out::println);
    }

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void testScanSpringboot() throws Exception {
//        System.out.println(applicationContext.getClass().getPackage().getName());
//        List<Class<?>> classes = ClassScanUtil.getAllClassByPackageName(ClickhouseJpaApplication.class.getPackage());
//        classes.forEach(System.out::println);
    }

    @Test
    public void testGetAllClassesWithAnnotation() throws Exception {
        List<Class<?>> classes = ClassScanUtil.getAllClassesWithAnnotation(applicationContext, ClickHouseEntity.class);
        classes.forEach(x -> System.out.println(x.getSimpleName()));
    }

}
