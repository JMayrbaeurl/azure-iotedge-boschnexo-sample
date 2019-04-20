package com.microsoft.samples.nexo.edgemodule;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * TighteningStepGraphEntry
 */
public class TighteningStepGraphEntry {

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
    
}