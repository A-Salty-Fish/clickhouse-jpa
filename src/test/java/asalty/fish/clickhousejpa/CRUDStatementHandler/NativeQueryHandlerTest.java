package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.NativeStatementHandler;
import asalty.fish.clickhousejpa.example.dao.CreateTableTestEntityDao;
import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/2 15:24
 */
@SpringBootTest
public class NativeQueryHandlerTest {

//    @Resource
//    NativeStatementHandler nativeStatementHandler;

    @Resource
    CreateTableTestEntityDao createTableTestEntityDao;

    @Test
    public void testSql() throws Exception {
        System.out.println(createTableTestEntityDao.countAll());
    }

}
