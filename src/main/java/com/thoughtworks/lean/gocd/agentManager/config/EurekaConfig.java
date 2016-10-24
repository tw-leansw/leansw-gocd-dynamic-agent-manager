package com.thoughtworks.lean.gocd.agentManager.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;

@Configuration
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@ConditionalOnProperty(name = "eureka.enabled")
public class EurekaConfig {
    @Override
    public String toString() {

        new Predicate<Integer>(){

            @Override
            public boolean test(Integer n) {
                return n>3;
            }
        };
        return "EurekaConfig{}";
    }

}
