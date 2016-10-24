package com.thoughtworks.lean.gocd.agentManager.domain;

/**
 * Created by yongliuli on 10/9/16.
 */
public class ScaleStrategy {

    private int minIdles;

    private int scaleStep;

    private int maxInstances;

    public int getMinIdles() {
        return minIdles;
    }

    public ScaleStrategy setMinIdles(int minIdles) {
        this.minIdles = minIdles;
        return this;
    }

    public int getScaleStep() {
        return scaleStep;
    }

    public ScaleStrategy setScaleStep(int scaleStep) {
        this.scaleStep = scaleStep;
        return this;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public ScaleStrategy setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
        return this;
    }

    public boolean shouldScaleUp(long idleCount) {
        return (idleCount) < this.getMinIdles();
    }

    public boolean shouldScaleDown(long idleCount) {
        return (idleCount > this.getScaleStep() &&
                (idleCount - this.getScaleStep()) >= this.getMinIdles());
    }
}
