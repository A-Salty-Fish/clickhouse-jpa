package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.InsertStatementHandler;
import asalty.fish.clickhousejpa.example.dao.CreateTableTestEntityDao;
import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;
import asalty.fish.clickhousejpa.example.entity.hits_v1;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/1 17:58
 */
@SpringBootTest
public class InsertStatementHandlerTest {

    @Resource
    private InsertStatementHandler insertStatementHandler;

    public CreateTableTestEntity getTestEntity() {
        CreateTableTestEntity h = new CreateTableTestEntity();
        h.setId(0L);
        h.setGoodEvent("goodEvent");
        h.setJavaEnable(false);
        h.setTitle("title");
        h.setWatchID(1L);
        h.setUserAgentMajor(222);
        h.setTestUserDefinedColumn("7777");
        return h;
    }

//    @Test
//    public void testInsertSql() throws Exception {
//        System.out.println(insertStatementHandler.getInsertSql(CreateTableTestEntity.class,getTestEntity()));
//    }

    @Test
    public void testResultHandler() throws Exception {
//        System.out.println(insertStatementHandler.resultHandler(
//                insertStatementHandler.getInsertSql(CreateTableTestEntity.class, getTestEntity()), CreateTableTestEntity.class));

    }

    @Resource
    CreateTableTestEntityDao createTableTestEntityDao;

    @Test
    public void testPostProcess() throws Exception {
        System.out.println(createTableTestEntityDao.create(getTestEntity()));
        System.out.println(createTableTestEntityDao.create(getTestEntity()));
        System.out.println(new Gson().toJson(createTableTestEntityDao.findAllByWatchID(getTestEntity().getWatchID())));
    }

    public CreateTableTestEntity getTestTimeEntity() {
        CreateTableTestEntity h = new CreateTableTestEntity();
        h.setId(0L);
        h.setGoodEvent("goodEvent");
        h.setJavaEnable(false);
        h.setTitle("title");
        h.setWatchID(1L);
        h.setUserAgentMajor(222);
        h.setTestUserDefinedColumn("7777");
        h.setCreateDay(LocalDate.now());
        h.setCreateTime(LocalDateTime.now());
        return h;
    }

    @Test
    public void testInsertTime() throws Exception {
        createTableTestEntityDao.create(getTestTimeEntity());
    }
}
