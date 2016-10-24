package com.thoughtworks.lean.gocd.agentManager.service;

import com.thoughtworks.lean.gocd.agentManager.domain.AgentConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentManagerConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;

import java.util.List;

public interface AgentService {

    List<AgentServiceInfo> getAgentServices();

    AgentConfig getConfigByName(String serviceName);

    List<AgentConfig> getAllConfig();

    AgentConfig updateConfig(AgentConfig config);

    ServiceInfo scaleUpInstancesByName(String serviceName);

    ServiceInfo scaleDownInstancesByName(String serviceName);

    void scaleUp(AgentServiceInfo agentServiceInfo);
    void scaleDown(AgentServiceInfo agentServiceInfo);

    ServiceInfo getServiceInfo(String serviceName);

    AgentManagerConfig getAgentManagerConfig();

    AgentManagerConfig updateAgentManagerConfig(AgentManagerConfig config);

    void addResourcesAndEnablePendingAgent(AgentServiceInfo agentServiceInfo);

    void autoScaleGoCDAgent();

    void deleteDisabledAgentAndStopInstance(AgentServiceInfo agentServiceInfo);
}