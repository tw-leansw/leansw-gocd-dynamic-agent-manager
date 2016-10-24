package com.thoughtworks.lean.gocd.agentManager.domain;

public class AgentStatistic {
    private int idle;
    private int building;
    private int pending;
    private int all;

    public int getAll() {
        return all;
    }

    public AgentStatistic setAll(int all) {
        this.all = all;
        return this;
    }

    public int getBuilding() {
        return building;
    }

    public AgentStatistic setBuilding(int building) {
        this.building = building;
        return this;
    }

    public int getIdle() {
        return idle;
    }

    public AgentStatistic setIdle(int idle) {
        this.idle = idle;
        return this;
    }

    public AgentStatistic plusIdle() {
        this.idle++;
        return this;
    }

    public AgentStatistic plusBuilding() {
        this.building++;
        return this;
    }

    public AgentStatistic plusPending() {
        this.pending++;
        return this;
    }

    public int getPending() {
        return pending;
    }
}
