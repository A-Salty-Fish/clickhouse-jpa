package asalty.fish.clickhousejpa.CRUDStatementHandler.Find;

import asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler.StatementHandler;
import asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler.StatementInterceptor;
import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import asalty.fish.clickhousejpa.mapper.ClickHouseMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 18:07
 */
@Configuration
@Slf4j
@Scope("prototype")
public class FindAllInterceptor implements StatementInterceptor {

    @Resource
    StatementHandler readStatementHandler;

    @Resource
    ClickHouseMapper clickHouseMapper;

    @Resource
    Statement clickHouseStatement;

    @Override
    public StatementHandler statementHandler() {
        return readStatementHandler;
    }


    @Override
    public Object resultHandler(String sql, Class<?> entity) throws Exception {
        return clickHouseMapper.convertResultSetToList(clickHouseStatement.executeQuery(sql), entity);
    }

}
