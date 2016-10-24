package com.thoughtworks.lean.gocd.agentManager.domain;

import com.google.common.base.Objects;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class AgentManagerConfig {

    @Id
    private long id;

    private int minIdles;

    private int maxInstances;

    private int scaleStep;

    private String agentStack;

    private String agentEnvironment;

    private String managedServiceResource;

    public String getManagedServiceResource() {
        return managedServiceResource;
    }

    public AgentManagerConfig setManagedServiceResource(String managedServiceResource) {
        this.managedServiceResource = managedServiceResource;
        return this;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public AgentManagerConfig setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
        return this;
    }

    public int getMinIdles() {
        return minIdles;
    }

    public AgentManagerConfig setMinIdles(int minIdles) {
        this.minIdles = minIdles;
        return this;
    }

    public int getScaleStep() {
        return scaleStep;
    }

    public AgentManagerConfig setScaleStep(int scaleStep) {
        this.scaleStep = scaleStep;
        return this;
    }

    public String getAgentStack() {
        return agentStack;
    }

    public AgentManagerConfig setAgentStack(String agentStack) {
        this.agentStack = agentStack;
        return this;
    }

    public String getAgentEnvironment() {
        return agentEnvironment;
    }

    public AgentManagerConfig setAgentEnvironment(String agentEnvironment) {
        this.agentEnvironment = agentEnvironment;
        return this;
    }

    public long getId() {
        return id;
    }

    public AgentManagerConfig setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "AgentManagerConfig{" +
                "managedServiceResource='" + managedServiceResource + '\'' +
                ", minIdles=" + minIdles +
                ", maxInstances=" + maxInstances +
                ", scaleStep=" + scaleStep +
                ", agentStack='" + agentStack + '\'' +
                ", agentEnvironment='" + agentEnvironment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentManagerConfig that = (AgentManagerConfig) o;
        return Objects.equal(agentStack, that.agentStack) &&
                Objects.equal(agentEnvironment, that.agentEnvironment) &&
                Objects.equal(managedServiceResource, that.managedServiceResource);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(agentStack, agentEnvironment, managedServiceResource);
    }
}
