package asalty.fish.clickhousejpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@Configuration("ClickhouseJpaConfiguration")
//@ComponentScan("asalty.fish.clickhousejpa")
public class ClickhouseJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClickhouseJpaApplication.class, args);
    }

}
