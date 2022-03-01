package asalty.fish.clickhousejpa.tableHandler;

import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;
import asalty.fish.clickhousejpa.example.entity.hits_v1;
import asalty.fish.clickhousejpa.exception.TableCreateException;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/1 13:23
 */
@SpringBootTest
public class testTableCreate {

    @Resource
    TableCreate tableCreate;

    @Test
    public void testGetCreateTableSql() throws TableCreateException, TypeNotSupportException {
        System.out.println(tableCreate.getCreateTableSql(hits_v1.class));
    }

    @Test
    public void testNeedCreate() throws TableCreateException {
        System.out.println(tableCreate.needCreateTable(hits_v1.class));
    }

    @Test
    public void testCreateTable() throws TableCreateException, TypeNotSupportException {
        tableCreate.createTable(CreateTableTestEntity.class);
    }
}
