package com.microsoft.samples.nexo.process;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TighteningStep
 */
public final class TighteningStep {

    private String result;
    private String name;
    @JsonProperty(value = "step type")
    private String steptype;
    private String row;
    private String column;
    private double category;
    @JsonProperty(value = "last cmd")
    private String lastcmd;
    private double torque;
    private double angle;
    private double duration;
    @JsonProperty(value = "quality code")
    private String qualitycode;
    private double speed;
    @JsonProperty(value = "angle threshold nom")
    private double anglethresholdnom;
    @JsonProperty(value = "angle threshold act")
    private double anglethresholdact;

    @JsonProperty(value = "tightening functions")
    private List<TighteningFunction> tighteningfunctions = new ArrayList<TighteningFunction>();
    private Graph graph;

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the steptype
     */
    public String getSteptype() {
        return steptype;
    }

    /**
     * @param steptype the steptype to set
     */
    public void setSteptype(String steptype) {
        this.steptype = steptype;
    }

    /**
     * @return the row
     */
    public String getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(String row) {
        this.row = row;
    }

    /**
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * @return the category
     */
    public double getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(double category) {
        this.category = category;
    }

    /**
     * @return the lastcmd
     */
    public String getLastcmd() {
        return lastcmd;
    }

    /**
     * @param lastcmd the lastcmd to set
     */
    public void setLastcmd(String lastcmd) {
        this.lastcmd = lastcmd;
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
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * @return the duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * @return the qualitycode
     */
    public String getQualitycode() {
        return qualitycode;
    }

    /**
     * @param qualitycode the qualitycode to set
     */
    public void setQualitycode(String qualitycode) {
        this.qualitycode = qualitycode;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the anglethresholdnom
     */
    public double getAnglethresholdnom() {
        return anglethresholdnom;
    }

    /**
     * @param anglethresholdnom the anglethresholdnom to set
     */
    public void setAnglethresholdnom(double anglethresholdnom) {
        this.anglethresholdnom = anglethresholdnom;
    }

    /**
     * @return the anglethresholdact
     */
    public double getAnglethresholdact() {
        return anglethresholdact;
    }

    /**
     * @param anglethresholdact the anglethresholdact to set
     */
    public void setAnglethresholdact(double anglethresholdact) {
        this.anglethresholdact = anglethresholdact;
    }

    /**
     * @return the tighteningfunctions
     */
    public List<TighteningFunction> getTighteningfunctions() {
        return tighteningfunctions;
    }

    /**
     * @param tighteningfunctions the tighteningfunctions to set
     */
    public void setTighteningfunctions(List<TighteningFunction> tighteningfunctions) {
        this.tighteningfunctions = tighteningfunctions;
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}