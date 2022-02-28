package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler.StatementHandler;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 20:49
 */
@Service
public class TestStatementHandler implements StatementHandler {
    @Override
    public boolean needHandle(Method method) {
        return method.getName().startsWith("test");
    }

    @Override
    public String getStatement(Method method, Object[] args, Class<?> entity) throws Exception {
        return null;
    }

    @Override
    public Object resultHandler(String sql, Class<?> entity) throws Exception {
        return System.currentTimeMillis();
    }
}
