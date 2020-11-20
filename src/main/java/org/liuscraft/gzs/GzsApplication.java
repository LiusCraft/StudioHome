package org.liuscraft.gzs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.liuscraft.gzs.mapper")
@SpringBootApplication
public class GzsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GzsApplication.class, args);
    }

}
