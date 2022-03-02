package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.CRUDStatementHandler.handler.InsertStatementHandler;
import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;
import asalty.fish.clickhousejpa.example.entity.hits_v1;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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

    @Test
    public void testInsertSql() throws Exception {
        System.out.println(insertStatementHandler.getInsertSql(CreateTableTestEntity.class,getTestEntity()));
    }

    @Test
    public void testResultHandler() throws Exception {
        System.out.println(insertStatementHandler.resultHandler(
                insertStatementHandler.getInsertSql(CreateTableTestEntity.class, getTestEntity()), CreateTableTestEntity.class));

    }
}
