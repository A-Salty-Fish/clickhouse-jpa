package asalty.fish.clickhousejpa.jdbc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author 13090
 * @version 1.0
 * @description: TODO
 * @date 2022/2/28 16:15
 */
@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "spring.jpa.clickhouse")
public class ClickHouseJdbcConfig {

    String driverClassName;

    String url;

    String port;

    String database;

    String username;

    String password;

    private String fullUrl() {
        return url + ":" + port + "/" + database;
    }

    private Connection connection;

    @Bean
    public Statement clickHouseStatement() throws Exception {
        Class.forName(driverClassName);
        log.info("clickhouse driver class name: {}", driverClassName);
        log.info("clickhouse url: {}", fullUrl());
        connection = DriverManager.getConnection(fullUrl(), username, password);
        log.info("clickhouse connection init");
        return connection.createStatement();
    }

}
