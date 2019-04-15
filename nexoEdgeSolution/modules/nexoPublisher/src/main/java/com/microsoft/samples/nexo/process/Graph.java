package com.microsoft.samples.nexo.process;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Graph
 */
public final class Graph {

    @JsonProperty(value = "angle values")
    List<Integer> angles = new ArrayList<Integer>();
    @JsonProperty(value = "torque values")
    List<Double> torques = new ArrayList<Double>();
    @JsonProperty(value = "time values")
    List<Double> timestamps = new ArrayList<Double>();

    /**
     * @return the angles
     */
    public List<Integer> getAngles() {
        return angles;
    }

    /**
     * @param angles the angles to set
     */
    public void setAngles(List<Integer> angles) {
        this.angles = angles;
    }

    /**
     * @return the torques
     */
    public List<Double> getTorques() {
        return torques;
    }

    /**
     * @param torques the torques to set
     */
    public void setTorques(List<Double> torques) {
        this.torques = torques;
    }

    /**
     * @return the timestamps
     */
    public List<Double> getTimestamps() {
        return timestamps;
    }

    /**
     * @param timestamps the timestamps to set
     */
    public void setTimestamps(List<Double> timestamps) {
        this.timestamps = timestamps;
    }

    
}