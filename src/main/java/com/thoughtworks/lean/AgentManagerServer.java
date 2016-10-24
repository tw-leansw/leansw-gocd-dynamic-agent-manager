package com.thoughtworks.lean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableCaching
@SpringBootApplication
@Configuration
@EnableFeignClients
@EnableAspectJAutoProxy
@ComponentScan
public class AgentManagerServer {

    public AgentManagerServer() {
        //default constructor
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AgentManagerServer.class, args);
    }

}
