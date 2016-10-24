package com.thoughtworks.lean.gocd.agentManager.domain;

import com.thoughtworks.lean.gocd.dto.AgentInfo;
import com.thoughtworks.lean.gocd.dto.constants.AgentConfigState;
import com.thoughtworks.lean.gocd.dto.constants.AgentState;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import com.thoughtworks.lean.rancher.dto.constants.ContainerStatus;

/**
 * Created by yongliuli on 10/9/16.
 */
public class CompositeAgentInfo {
    private ServiceInstance serviceInstance;
    private AgentInfo agentInfo;


    public boolean isAvailable() {
        return (isAgentIdle() &&
                isAgentEnabled())
                || (agentInfo == null && isInstanceRunning());
    }

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public CompositeAgentInfo setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
        return this;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public CompositeAgentInfo setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
        return this;
    }


    public boolean isInstanceRunning() {
        return getServiceInstance().getState().equals(ContainerStatus.RUNNING);
    }
    public boolean isInstanceStarting() {
        return getServiceInstance().getState().equalsIgnoreCase(ContainerStatus.STARTING);
    }

    public boolean isGoCDAgentPending() {
        return getAgentInfo() != null && getAgentInfo().getAgentConfigState().equalsIgnoreCase(AgentState.PENDING);
    }

    public boolean isResourceAdded(AgentManagerConfig agentManagerConfig) {
        return getAgentInfo() != null && getAgentInfo().getResources().contains(agentManagerConfig.getManagedServiceResource());
    }

    public boolean isAgentInstanceStopped() {
        return getServiceInstance().getState().equalsIgnoreCase(ContainerStatus.STOPED);
    }

    public boolean isAgentIdle() {
        return getAgentInfo() != null && getAgentInfo().getAgentState().equalsIgnoreCase(AgentState.IDLE);
    }

    public boolean isAgentBuilding() {
        return getAgentInfo() != null && getAgentInfo().getAgentConfigState().equalsIgnoreCase(AgentState.BUILDING);
    }

    public boolean isAgentDisabled() {
        return getAgentInfo() != null && getAgentInfo().getAgentConfigState().equalsIgnoreCase(AgentConfigState.DISABLED);
    }

    public boolean isAgentEnabled() {
        return getAgentInfo() != null && getAgentInfo().getAgentConfigState().equalsIgnoreCase(AgentConfigState.DISABLED);
    }

    @Override
    public String toString() {
        return "CompositeAgentInfo{" +
                "serviceInstance=" + serviceInstance +
                ", agentInfo=" + agentInfo +
                '}';
    }


}
