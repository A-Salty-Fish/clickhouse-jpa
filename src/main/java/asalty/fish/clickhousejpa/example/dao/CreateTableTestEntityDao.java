package asalty.fish.clickhousejpa.example.dao;

import asalty.fish.clickhousejpa.annotation.ClickHouseRepository;
import asalty.fish.clickhousejpa.example.entity.CreateTableTestEntity;

import java.util.List;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/3/2 11:11
 */
@ClickHouseRepository(entity = CreateTableTestEntity.class)
public class CreateTableTestEntityDao {

    public List<CreateTableTestEntity> findAllByWatchID(Long watchID) {
        return null;
    }

    public Boolean create(CreateTableTestEntity entity) {
        return null;
    }

}