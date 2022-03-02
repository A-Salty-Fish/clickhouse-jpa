package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.StatisticsStatementHandler;
import asalty.fish.clickhousejpa.example.dao.CreateTableTestEntityDao;
import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/2 17:36
 */
@SpringBootTest
public class StatisticsStatementHandlerTest {

    @Resource
    CreateTableTestEntityDao createTableTestEntityDao;

    @Resource
    StatisticsStatementHandler statisticsStatementHandler;

    public void countOne() {

    }

    @Test
    public void testStatementHandler() throws Exception {
        System.out.println(statisticsStatementHandler.getStatement(CreateTableTestEntityDao.class.getMethod("countWatchIDByWatchID", Long.class), new Object[]{1L}, CreateTableTestEntity.class));
    }
}
