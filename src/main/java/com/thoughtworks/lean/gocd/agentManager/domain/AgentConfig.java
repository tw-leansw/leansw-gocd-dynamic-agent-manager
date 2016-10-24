package com.thoughtworks.lean.gocd.agentManager.domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AgentConfig {
    @Id
    private Long id;

    private String name;

    private int minIdles;

    private int scaleStep;

    private int maxInstances;

    private boolean autoConfig;

    private Set<String> resources;

    private String projectName;

    protected AgentConfig() {
    }

    public AgentConfig(String name, int minIdele, int scaleStep, int maxInstances, boolean autoConfig, String projectName) {
        this.name = name;
        this.minIdles = minIdele;
        this.scaleStep = scaleStep;
        this.maxInstances = maxInstances;
        this.autoConfig = autoConfig;
        this.projectName = projectName;
        this.resources = new HashSet<>();
    }


    public Long getId() {
        return id;
    }

    public AgentConfig setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AgentConfig setName(String name) {
        this.name = name;
        return this;
    }

    public int getMinIdles() {
        return minIdles;
    }

    public AgentConfig setMinIdles(int minIdles) {
        this.minIdles = minIdles;
        return this;
    }

    public int getScaleStep() {
        return scaleStep;
    }

    public AgentConfig setScaleStep(int scaleStep) {
        this.scaleStep = scaleStep;
        return this;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public AgentConfig setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
        return this;
    }

    public boolean isAutoConfig() {
        return autoConfig;
    }

    public AgentConfig setAutoConfig(boolean autoConfig) {
        this.autoConfig = autoConfig;
        return this;
    }

    public Set<String> getResources() {
        return resources == null ? Collections.emptySet() : resources;
    }

    public AgentConfig setResources(Set<String> resources) {
        this.resources = resources;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public AgentConfig setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public AgentConfig addResource(String resource) {
        this.resources.add(resource);
        return this;
    }


    @Override
    public String toString() {
        return "AgentConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minIdles=" + minIdles +
                ", scaleStep=" + scaleStep +
                ", maxInstances=" + maxInstances +
                ", autoConfig=" + autoConfig +
                ", resources='" + resources + '\'' +
                '}';
    }
}
