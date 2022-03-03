package asalty.fish.clickhousejpa.jdbc;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
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

    private DataSource dataSource;

    private ThreadLocal<Statement> statement = new ThreadLocal<>();
    @Resource
    HikariConfig hikariConfig;

    @Bean
    public Statement clickHouseStatement() throws Exception {
        if (dataSource == null) {
            System.out.println(new Gson().toJson(hikariConfig));
            hikariConfig.setJdbcUrl(fullUrl());
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource.getConnection().createStatement();
    }

    public Statement getNewStatement() throws Exception {
        if (dataSource == null) {
            System.out.println(new Gson().toJson(hikariConfig));
            hikariConfig.setJdbcUrl(fullUrl());
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource.getConnection().createStatement();
    }

    public Statement threadLocalStatement() throws Exception {
        if (statement.get() == null) {
            Statement s = getNewStatement();
            statement.set(s);
        }
        return statement.get();
    }

    @Bean
    public Connection clickHouseConnection() throws Exception {
        return dataSource.getConnection();
    }

}
