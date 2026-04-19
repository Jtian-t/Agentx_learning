package com.jin.agentx_learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jin.agentx_learning")
public class AgentXLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentXLearningApplication.class, args);
    }

}
