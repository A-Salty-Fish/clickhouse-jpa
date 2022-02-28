package asalty.fish.clickhousejpa.CRUDStatementHandler.Find;

import asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler.StatementInterceptor;
import asalty.fish.clickhousejpa.CRUDStatementHandler.statementHandler.StatementProxy;
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
public class FindAllStatementProxy implements StatementProxy {

    @Resource
    StatementInterceptor findAllInterceptor;

    @Override
    public StatementInterceptor statementInterceptor() {
        return findAllInterceptor;
    }
}
