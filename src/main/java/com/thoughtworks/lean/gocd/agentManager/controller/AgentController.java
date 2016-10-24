package com.thoughtworks.lean.gocd.agentManager.controller;

import com.thoughtworks.lean.gocd.agentManager.domain.AgentConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentManagerConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentServiceInfo;
import com.thoughtworks.lean.gocd.agentManager.service.AgentService;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping(path = {"/api/gocd-agent/"})
public class AgentController {

    @Autowired
    private AgentService agentService;


    ThreadPoolTaskExecutor taskExecutor;

    {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.initialize();
    }


    @RequestMapping(value = "/all", method = GET)
    public List<AgentServiceInfo> getAgentServices() {
        return agentService.getAgentServices();
    }

    public AgentController setAgentService(AgentService agentService) {
        this.agentService = agentService;
        return this;
    }

    @RequestMapping(value = "/update", method = POST)
    public AgentConfig updateAgentServiceConfig(@RequestBody AgentConfig config) {
        return this.agentService.updateConfig(config);
    }

    @RequestMapping(value = "/config/{agentName}")
    public AgentConfig getAgentServiceConfig(@PathVariable("agentName") String agentName) {
        return this.agentService.getConfigByName(agentName);
    }

    @RequestMapping(value = "/scaleup/{agentName}", method = POST)
    public ServiceInfo scaleUpInstances(@PathVariable("agentName") String agentName) {
        taskExecutor.execute(() -> this.agentService.scaleUpInstancesByName(agentName));
        return this.agentService.getServiceInfo(agentName);
    }

    @RequestMapping(value = "/scaledown/{agentName}", method = POST)
    public ServiceInfo scaleDownInstances(@PathVariable("agentName") String agentName) {
        taskExecutor.execute(() -> this.agentService.scaleDownInstancesByName(agentName));
        return this.agentService.getServiceInfo(agentName);
    }

    @RequestMapping(value = "/env", method = PUT)
    public AgentManagerConfig updateAgentEnv(@RequestBody AgentManagerConfig config) {
        return this.agentService.updateAgentManagerConfig(config);
    }

    @RequestMapping(value = "/env", method = GET)
    public AgentManagerConfig getAgentEnv() {
        return this.agentService.getAgentManagerConfig();
    }


    @RequestMapping(value = "/auto_scale")
    public String autoScale() {
        taskExecutor.execute(() -> agentService.autoScaleGoCDAgent());
        return "Auto Scale Complete!";
    }


}
