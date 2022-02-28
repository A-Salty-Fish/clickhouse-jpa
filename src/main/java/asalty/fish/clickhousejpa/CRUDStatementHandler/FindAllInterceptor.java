package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import asalty.fish.clickhousejpa.example.dao.HitsV1Dao;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 18:07
 */
@Configuration
@Scope("prototype")
public class FindAllInterceptor implements MethodInterceptor {

    @Resource
    ReadStatementHandler readStatementHandler;

    @Resource
    ClickHouseMapper clickHouseMapper;

    @Resource
    Statement clickHouseStatement;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (method.getName().startsWith("findAllBy")) {
            ClickHouseRepository clickHouseRepository = method.getDeclaringClass().getAnnotation(ClickHouseRepository.class);
            if (clickHouseRepository != null) {
                String[] sqlArgs = new String[args.length];
                for (int i = 0; i < args.length; i++) {
                    sqlArgs[i] = args[i].toString();
                }
                String sql = readStatementHandler.prepareFindAllSQL(clickHouseRepository.entity(), method.getName(), sqlArgs);
                System.out.println(sql);
                return clickHouseMapper.convertResultSetToList(clickHouseStatement.executeQuery(sql), clickHouseRepository.entity());
            }
        }
        return methodProxy.invokeSuper(obj, args);
    }
}
