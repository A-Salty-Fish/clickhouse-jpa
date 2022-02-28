package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.Find.FindAllInterceptor;
import asalty.fish.clickhousejpa.CRUDStatementHandler.Find.FindAllStatementProxy;
import asalty.fish.clickhousejpa.CRUDStatementHandler.Find.ReadStatementHandler;
import asalty.fish.clickhousejpa.example.dao.HitsV1Dao;
import asalty.fish.clickhousejpa.example.entity.hits_v1;
import com.google.gson.Gson;
import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 16:46
 */
@SpringBootTest
public class ReadStatementHandlerTest {

    @Resource
    ReadStatementHandler readStatementHandler;

    @Test
    public void testReadLimit() throws Exception {
        List<hits_v1> allLimitedBy = readStatementHandler.findAllLimitedBy(hits_v1.class, 20);
        Assertions.assertEquals(allLimitedBy.size(), 20);
        System.out.println(new Gson().toJson(allLimitedBy));
    }

    @Test
    public void testGetFindAllSqlFromMethodName() throws Exception {
        System.out.println(readStatementHandler.getFindAllSqlFromMethodName(hits_v1.class, "findAllBy1And2Or3And4Or5And6"));
    }

    @Test
    public void testPrepareFindAllSQL() throws Exception {
        System.out.println(readStatementHandler.prepareFindAllSQL(hits_v1.class, "findAllBy1And2Or3And4Or5And6",
                new String[]{"1", "2", "3", "4", "5", "6"}));
    }

    @Resource
    FindAllInterceptor findAllInterceptor;

    @Test
    public void testPrepareFindAllInterceptor() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HitsV1Dao.class);
        enhancer.setCallback(findAllInterceptor);
        HitsV1Dao hitsV1Dao = (HitsV1Dao) enhancer.create();
        System.out.println(hitsV1Dao.findAllByWatchID(Long.valueOf("4944118417295196513")));
    }

    @Resource
    FindAllStatementProxy findAllStatementProxy;

    @Test
    public void testFindAllProxy() throws Exception {
        HitsV1Dao hitsV1Dao = findAllStatementProxy.getProxy(HitsV1Dao.class);
        System.out.println(hitsV1Dao.findAllByWatchID(Long.valueOf("4944118417295196513")));
    }

    @Resource
    HitsV1Dao hitsV1Dao;

    @Test
    public void testInjection() throws Exception {
        System.out.println(hitsV1Dao);
        System.out.println(hitsV1Dao.findAllByWatchID(Long.valueOf("4944118417295196513")));
    }
}
