package asalty.fish.clickhousejpa.example.entity;

import asalty.fish.clickhousejpa.annotation.ClickHouseColumn;
import asalty.fish.clickhousejpa.annotation.ClickHouseEngine;
import asalty.fish.clickhousejpa.annotation.ClickHouseEntity;
import asalty.fish.clickhousejpa.annotation.ClickHouseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 14:46
 */
@Data
@ClickHouseEntity
@ClickHouseTable(name = "hits_v1", engine = ClickHouseEngine.MergeTree)
public class hits_v1 {

//    @ClickHouseColumn(isPrimaryKey = true)
//    public Long id;

    @ClickHouseColumn(comment = "观看id")
    public Long WatchID;

    public Boolean JavaEnable;

    @ClickHouseColumn(comment = "标题")
    public String Title;

    public String GoodEvent;

    public Integer UserAgentMajor;

    @ClickHouseColumn(name = "URLDomain")
    public String testUserDefinedColumn;

    public hits_v1() {

    }
}
