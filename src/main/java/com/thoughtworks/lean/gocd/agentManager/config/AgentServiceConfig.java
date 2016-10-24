package com.thoughtworks.lean.gocd.agentManager.config;

import com.thoughtworks.lean.gocd.agentManager.domain.AgentManagerConfig;
import com.thoughtworks.lean.gocd.agentManager.repository.AgentManagerConfigRepository;
import com.thoughtworks.lean.gocd.agentManager.service.AgentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AgentServiceConfig {

    public static int MANAGER_CONFIG_DEFAULT_ID = 1;


    @Value("${gocd.agentManager.agentEnvironment}")
    private String agentEnvironment;
    @Value("${gocd.agentManager.agentStack}")
    private String agentStack;
    @Value("${gocd.agentManager.managedServiceResource}")
    private String managedServiceResource;
    @Value("${gocd.agentManager.minIdles}")
    private int minIdles;
    @Value("${gocd.agentManager.maxInstances}")
    private int maxInstances;
    @Value("${gocd.agentManager.scaleStep}")
    private int scaleStep;


    @Autowired
    private AgentManagerConfigRepository managerConfigRepository;

    @Bean
    @Lazy
    @Qualifier("agentService")
    public AgentServiceImpl configAgentService() {
        AgentManagerConfig agentManagerConfig = managerConfigRepository.findOneById(MANAGER_CONFIG_DEFAULT_ID);
        AgentServiceImpl agentService = new AgentServiceImpl();
        if (agentManagerConfig == null) {
            agentManagerConfig = new AgentManagerConfig()
                    .setId(MANAGER_CONFIG_DEFAULT_ID)
                    .setAgentEnvironment(agentEnvironment)
                    .setAgentStack(agentStack)
                    .setManagedServiceResource(managedServiceResource)
                    .setMaxInstances(maxInstances)
                    .setMinIdles(minIdles)
                    .setScaleStep(scaleStep);
            managerConfigRepository.save(agentManagerConfig);
        }
        return agentService;
    }


}
