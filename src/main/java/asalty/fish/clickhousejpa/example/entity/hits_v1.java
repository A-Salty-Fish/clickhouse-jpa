package asalty.fish.clickhousejpa.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 14:46
 */
@Data
public class hits_v1 {

    public Long WatchID;

    public boolean JavaEnable;

    public String Title;

    public String GoodEvent;

    public Integer UserAgentMajor;

    public hits_v1() {

    }
}
