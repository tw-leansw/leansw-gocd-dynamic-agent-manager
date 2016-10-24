package com.thoughtworks.lean.gocd.agentManager.domain;

import com.thoughtworks.lean.rancher.dto.ServiceInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AgentServiceInfo {
    private String serviceName;
    private AgentConfig config;
    private AgentManagerConfig agentManagerConfig;
    private ServiceInfo serviceInfo;
    private List<CompositeAgentInfo> compositeAgentInfos;


    public AgentServiceInfo() {
    }


    public ScaleStrategy getScaleStrategy() {
        return new ScaleStrategy()
                .setMinIdles((config != null) ? config.getMinIdles() : agentManagerConfig.getMinIdles())
                .setMaxInstances((config != null) ? config.getMaxInstances() : agentManagerConfig.getMaxInstances())
                .setScaleStep((config != null) ? config.getScaleStep() : agentManagerConfig.getScaleStep());
    }


    public AgentManagerConfig getAgentManagerConfig() {
        return agentManagerConfig;
    }

    public AgentServiceInfo setAgentManagerConfig(AgentManagerConfig agentManagerConfig) {
        this.agentManagerConfig = agentManagerConfig;
        return this;
    }

    private long getIdleCount() {
        return getIdle();
    }

    public boolean shouldScaleUp() {
        return getScaleStrategy().shouldScaleUp(getIdle());
    }

    public boolean shouldScaleDown() {
        return getScaleStrategy().shouldScaleDown(getIdle());
    }

    public String getServiceName() {
        return serviceName;
    }

    public AgentServiceInfo setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public Set<String> getResources() {
        Set<String> resources = new HashSet<>();
        resources.add(agentManagerConfig.getManagedServiceResource());
        resources.add(getServiceResourceTag());
        resources.addAll(config.getResources());
        return resources;
    }


    public AgentConfig getConfig() {
        return config;
    }

    public AgentServiceInfo setConfig(AgentConfig config) {
        this.config = config;
        return this;
    }

    private long countAgents(String status) {
        return this.compositeAgentInfos.stream()
                .filter(compositeAgentInfo -> compositeAgentInfo.getAgentInfo() != null
                        && status.equalsIgnoreCase(compositeAgentInfo.getAgentInfo().getAgentState()))
                .count();
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public AgentServiceInfo setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
        return this;
    }

    public List<CompositeAgentInfo> getCompositeAgentInfos() {
        return compositeAgentInfos;
    }

    public AgentServiceInfo setCompositeAgentInfos(List<CompositeAgentInfo> compositeAgentInfos) {
        this.compositeAgentInfos = compositeAgentInfos;
        return this;
    }

    public long getBuilding() {
        return getCompositeAgentInfos().stream()
                .filter(CompositeAgentInfo::isAgentBuilding)
                .count();
    }

    public long getIdle() {
        return getCompositeAgentInfos().stream()
                .filter(compositeAgentInfo -> compositeAgentInfo.getServiceInstance().isRunning() &&
                        (compositeAgentInfo.getAgentInfo() == null || compositeAgentInfo.isAgentIdle()))
                .count();
    }

    public int getMinIdles() {
        return getScaleStrategy().getMinIdles();
    }

    public int getScaleStep() {
        return getScaleStrategy().getScaleStep();
    }

    public int getMaxInstances() {
        return this.getScaleStrategy().getMaxInstances();
    }

    public String getRancherStatus() {
        return this.serviceInfo.getState();
    }

    public int getScale() {
        return this.serviceInfo.getScale();
    }

    public String getServiceResourceTag() {
        return "rancher-" + this.config.getProjectName() + "-" + this.getServiceName();
    }

    public boolean isAllAgentAvailable() {
        return getCompositeAgentInfos().stream().allMatch(compositeAgentInfo -> compositeAgentInfo.getAgentInfo() != null);
    }
}
