package com.thoughtworks.lean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@EnableCaching
@SpringBootApplication
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
@Controller
public class AgentManagerServer {
    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/ui/gocd-agent/list");
    }
    public AgentManagerServer() {
        //default constructor
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AgentManagerServer.class, args);
    }

}
