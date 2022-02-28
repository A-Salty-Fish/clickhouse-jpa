package asalty.fish.clickhousejpa.jdbc;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 13090
 * @version 1.0
 * @description: hikari连接池配置
 * @date 2022/2/28 22:49
 */
@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "spring.jpa.clickhouse.hikari")
public class HikariCPConfig {

    private String maximumPoolSize;

    private String idleTimeout;

    private String connectionTimeout;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
        config.setIdleTimeout(Long.parseLong(idleTimeout));
        config.setConnectionTimeout(Long.parseLong(connectionTimeout));
        return config;
    }
}
