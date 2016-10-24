package com.thoughtworks.lean.gocd.agentManager.domain;

public class ServiceInstanceStatistic {
    private int running;
    private int stopped;
    private int stopping;
    private int starting;

    public int getRunning() {
        return running;
    }

    public ServiceInstanceStatistic setRunning(int running) {
        this.running = running;
        return this;
    }

    public int getStopped() {
        return stopped;
    }

    public ServiceInstanceStatistic setStopped(int stopped) {
        this.stopped = stopped;
        return this;
    }

    public int getStopping() {
        return stopping;
    }

    public ServiceInstanceStatistic setStopping(int stopping) {
        this.stopping = stopping;
        return this;
    }

    public int getStarting() {
        return starting;
    }

    public ServiceInstanceStatistic setStarting(int starting) {
        this.starting = starting;
        return this;
    }
}
