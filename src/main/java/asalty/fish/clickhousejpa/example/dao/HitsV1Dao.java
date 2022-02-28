package asalty.fish.clickhousejpa.example.dao;

import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import asalty.fish.clickhousejpa.example.entity.hits_v1;

import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 14:48
 */
@ClickHouseRepository(entity = hits_v1.class)
public class HitsV1Dao {

    public List<hits_v1> findAllByWatchID(Long watchId) {
        return null;
    }

    public Object testHandler(String s) {
        return null;
    }
}
