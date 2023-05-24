package com;

import com.common.Tools;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
@MapperScan({"com.mapper"})
public class ApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, args);
    }
    @PreDestroy
    public void destroy(){
        Tools.stopALLTask();
    }
}
