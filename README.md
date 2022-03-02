# clickhouse-jpa

由于毕设需要使用clickhouse，  
但是spring data jpa不支持clickhouse，（拒绝mybatis的xml）  
只能自己写一个风格类似的先凑合用了。（以后有空再写个jpa规范版）

## 使用方法

1. 添加依赖
2. 填写配置文件
```yaml
spring:
  jpa:
    clickhouse:
      # clickhouse引擎
      driver-class-name: com.github.housepower.jdbc.ClickHouseDriver
      # clickhouse的url
      url: jdbc:clickhouse://localhost
      # clickhouse的端口
      port: 19000
      # clickhouse的数据库
      database: tutorial
      # clickhouse的用户名
      username: default
      # clickhouse的密码
      password:
      # 是否开启自动创建表\更新表
      table-update: true
      # 连接池配置
      hikari:
        # 连接池的最大连接数
        maximum-pool-size: 10
        # 空闲超时
        idleTimeout: 1000000
        # 连接超时时间
        connection-timeout: 5000
```
3. 新建一个实体类（若开启自动建表，会在容器启动时自动创建该实体对应的表）
```java
@Data
@ClickHouseEntity
@ClickHouseTable(name = "create_table_test_entity", engine = ClickHouseEngine.MergeTree)
public class CreateTableTestEntity {

    @ClickHouseColumn(isPrimaryKey = true)
    public Long id;

    @ClickHouseColumn(comment = "观看id")
    public Long WatchID;

    public Boolean JavaEnable;

    @ClickHouseColumn(comment = "标题")
    public String Title;

    public String GoodEvent;

    public Integer UserAgentMajor;

    @ClickHouseColumn(name = "URLDomain")
    public String testUserDefinedColumn;

    public LocalDateTime CreateTime;

    public LocalDate CreateDay;

    public CreateTableTestEntity() {

    }
}
```
4. 新建一个dao（需要在注解中指定实体，方法内的逻辑随意填写）
```java
@ClickHouseRepository(entity = CreateTableTestEntity.class)
public class CreateTableTestEntityDao {

    public List<CreateTableTestEntity> findAllByWatchID(Long watchID) {
        return null;
    }

    public Boolean create(CreateTableTestEntity entity) {
        return null;
    }

    @ClickHouseNativeQuery("select count(*) from create_table_test_entity")
    public Long countAll() {
        return null;
    }

    public Long countWatchIDByWatchID(Long watchID) {
        return null;
    }

    public Long countWatchIDByWatchIDAndTitle(Long watchID, String title) {
        return null;
    }
}
```
5. 将dao注入到spring容器中使用
6. 开启自动建表后生成的表：
![img.png](img.png)

## 注解文档
### ClickHouseEntity
用于标记实体类
### ClickHouseTable
用于标记表名，默认为类名，可以指定表引擎，暂时只支持MergeTree
### ClickHouseRepository
用于标记dao，需要在注解中指定dao对应的实体类
### ClickHouseColumn
用于提供列的额外信息：列名（默认为字段名），是否为主键，注释内容（注释暂不支持写入表）。
### ClickHouseNativeQuery
用于标记原生sql查询，需要在注解中指定sql语句

## 现存的问题
1. 没写防注入的逻辑（clickhouse-jdbc就不支持，自己写太累了）
2. 自动生成的查询方法暂时只支持list返回值
3. 自动生成的查询方法暂时只支持And和Or查询
4. 自动生成的查询方法没有做大小写和列转换，所以需要保证方法名中的列名和实体类中的列名一致
5. 自动生成的插入方法暂时只支持单个对象插入
6. 没写更新和删除（好像也用不到）
7. 暂时支持的列类型只有：
```java
javaTypeToClickhouseMap.put(Long.class.getSimpleName(), "UInt64");
javaTypeToClickhouseMap.put(Integer.class.getSimpleName(), "UInt32");
javaTypeToClickhouseMap.put(Boolean.class.getSimpleName(), "UInt8");
javaTypeToClickhouseMap.put(String.class.getSimpleName(), "String");
javaTypeToClickhouseMap.put(LocalDateTime.class.getSimpleName(), "DateTime");
javaTypeToClickhouseMap.put(LocalDate.class.getSimpleName(), "Date");
javaTypeToClickhouseMap.put(Double.class.getSimpleName(), "Float64");
```

