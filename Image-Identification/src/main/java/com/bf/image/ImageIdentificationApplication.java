package com.bf.image;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bf.image.mapper")
public class ImageIdentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageIdentificationApplication.class, args);
    }

}
