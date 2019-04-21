package com.microsoft.samples.nexo.edgemodule;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * TighteningStepGraphEntry
 */
public class TighteningStepGraphEntry {

    private String idcode;
    private int cycle;
    private String steprow;
    private String stepcol;
    
    private int stepIndex;
    private int graphIndex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date timestamp;
    private int angle;
    private double torque;

    public TighteningStepGraphEntry(final Date ts, int anglevalue, double torquevalue) {
        this.timestamp = ts;
        this.angle = anglevalue;
        this.torque = torquevalue;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

    /**
     * @return the torque
     */
    public double getTorque() {
        return torque;
    }

    /**
     * @param torque the torque to set
     */
    public void setTorque(double torque) {
        this.torque = torque;
    }

    /**
     * @return the idcode
     */
    public String getIdcode() {
        return idcode;
    }

    /**
     * @param idcode the idcode to set
     */
    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    /**
     * @return the cycle
     */
    public int getCycle() {
        return cycle;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    /**
     * @return the steprow
     */
    public String getSteprow() {
        return steprow;
    }

    /**
     * @param steprow the steprow to set
     */
    public void setSteprow(String steprow) {
        this.steprow = steprow;
    }

    /**
     * @return the stepcol
     */
    public String getStepcol() {
        return stepcol;
    }

    /**
     * @param stepcol the stepcol to set
     */
    public void setStepcol(String stepcol) {
        this.stepcol = stepcol;
    }

    /**
     * @return the stepIndex
     */
    public int getStepIndex() {
        return stepIndex;
    }

    /**
     * @param stepIndex the stepIndex to set
     */
    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    /**
     * @return the graphIndex
     */
    public int getGraphIndex() {
        return graphIndex;
    }

    /**
     * @param graphIndex the graphIndex to set
     */
    public void setGraphIndex(int graphIndex) {
        this.graphIndex = graphIndex;
    }
    
}