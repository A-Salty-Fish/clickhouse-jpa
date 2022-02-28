package asalty.fish.clickhousejpa.CRUDStatementHandler;

import asalty.fish.clickhousejpa.example.entity.hits_v1;
import com.google.gson.Gson;
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

}
