package asalty.fish.clickhousejpa.mapper;

import asalty.fish.clickhousejpa.example.entity.hits_v1;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 16:12
 */
@SpringBootTest
public class MapperTest {

    @Resource
    ClickHouseMapper clickHouseMapper;

    @Resource
    Statement clickHouseStatement;

    @Test
    public void testMapper() throws Exception {
        try {
            ResultSet rs = clickHouseStatement.executeQuery("select * from tutorial.hits_v1 limit 10");
            List<hits_v1> list = clickHouseMapper.convertResultSetToList(rs, hits_v1.class);
            System.out.println(new Gson().toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
