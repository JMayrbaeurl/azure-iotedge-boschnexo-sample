package com.microsoft.samples.nexo.process;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TighteningProcess
 */
public class TighteningProcess {

    private int nr;
    private String result;
    private String channel;
    @JsonProperty(value = "prg nr")
    private int prgnr;
    @JsonProperty(value = "prg name")
    private String prgname;
    @JsonProperty(value = "prg date")
    private String prgdate;
    private int cycle;
    @JsonProperty(value = "nominal torque")
    private double nominaltorque;
    private String date;
    @JsonProperty(value = "id code")
    private String idcode;
    @JsonProperty(value = "torque unit")
    private String torqueunit;
    @JsonProperty(value = "last cmd")
    private String lastcmd;
    @JsonProperty(value = "quality code")
    private String qualitycode;
    @JsonProperty(value = "total time")
    private String totaltime;
    @JsonProperty(value = "tool serial")
    private String toolserial;
    @JsonProperty(value = "rework code")
    private int reworkcode;
    @JsonProperty(value = "rework text")
    private String reworktext;
    @JsonProperty(value = "cell id")
    private String cellid;
    @JsonProperty(value = "job nr")
    private int jobnr;
    @JsonProperty(value = "MCE factor")
    private double MCEfactor;
    @JsonProperty(value = "batch nr")
    private String batchnr;
    @JsonProperty(value = "batch canceled")
    private int batchcanceled;
    @JsonProperty(value = "batch direction OK")
    private int batchdirectionOK;
    @JsonProperty(value = "batch direction NOK")
    private int batchdirectionNOK;
    @JsonProperty(value = "batch max OK")
    private int batchmaxOK;
    @JsonProperty(value = "batch max NOK")
    private int batchmaxNOK;
    @JsonProperty(value = "batch OK")
    private int batchOK;
    @JsonProperty(value = "batch NOK")
    private int batchNOK;

    @JsonProperty(value = "tightening steps")
    private List <TighteningStep> tighteningsteps = new ArrayList <TighteningStep> ();

    /**
     * @return the nr
     */
    public int getNr() {
        return nr;
    }

    /**
     * @param nr the nr to set
     */
    public void setNr(int nr) {
        this.nr = nr;
    }

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
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the prgnr
     */
    public int getPrgnr() {
        return prgnr;
    }

    /**
     * @param prgnr the prgnr to set
     */
    public void setPrgnr(int prgnr) {
        this.prgnr = prgnr;
    }

    /**
     * @return the prgname
     */
    public String getPrgname() {
        return prgname;
    }

    /**
     * @param prgname the prgname to set
     */
    public void setPrgname(String prgname) {
        this.prgname = prgname;
    }

    /**
     * @return the prgdate
     */
    public String getPrgdate() {
        return prgdate;
    }

    /**
     * @param prgdate the prgdate to set
     */
    public void setPrgdate(String prgdate) {
        this.prgdate = prgdate;
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
     * @return the nominaltorque
     */
    public double getNominaltorque() {
        return nominaltorque;
    }

    /**
     * @param nominaltorque the nominaltorque to set
     */
    public void setNominaltorque(double nominaltorque) {
        this.nominaltorque = nominaltorque;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
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
     * @return the torqueunit
     */
    public String getTorqueunit() {
        return torqueunit;
    }

    /**
     * @param torqueunit the torqueunit to set
     */
    public void setTorqueunit(String torqueunit) {
        this.torqueunit = torqueunit;
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
     * @return the totaltime
     */
    public String getTotaltime() {
        return totaltime;
    }

    /**
     * @param totaltime the totaltime to set
     */
    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    /**
     * @return the toolserial
     */
    public String getToolserial() {
        return toolserial;
    }

    /**
     * @param toolserial the toolserial to set
     */
    public void setToolserial(String toolserial) {
        this.toolserial = toolserial;
    }

    /**
     * @return the reworkcode
     */
    public int getReworkcode() {
        return reworkcode;
    }

    /**
     * @param reworkcode the reworkcode to set
     */
    public void setReworkcode(int reworkcode) {
        this.reworkcode = reworkcode;
    }

    /**
     * @return the reworktext
     */
    public String getReworktext() {
        return reworktext;
    }

    /**
     * @param reworktext the reworktext to set
     */
    public void setReworktext(String reworktext) {
        this.reworktext = reworktext;
    }

    /**
     * @return the cellid
     */
    public String getCellid() {
        return cellid;
    }

    /**
     * @param cellid the cellid to set
     */
    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    /**
     * @return the jobnr
     */
    public int getJobnr() {
        return jobnr;
    }

    /**
     * @param jobnr the jobnr to set
     */
    public void setJobnr(int jobnr) {
        this.jobnr = jobnr;
    }

    /**
     * @return the mCEfactor
     */
    public double getMCEfactor() {
        return MCEfactor;
    }

    /**
     * @param mCEfactor the mCEfactor to set
     */
    public void setMCEfactor(double mCEfactor) {
        MCEfactor = mCEfactor;
    }

    /**
     * @return the batchnr
     */
    public String getBatchnr() {
        return batchnr;
    }

    /**
     * @param batchnr the batchnr to set
     */
    public void setBatchnr(String batchnr) {
        this.batchnr = batchnr;
    }

    /**
     * @return the batchcanceled
     */
    public int getBatchcanceled() {
        return batchcanceled;
    }

    /**
     * @param batchcanceled the batchcanceled to set
     */
    public void setBatchcanceled(int batchcanceled) {
        this.batchcanceled = batchcanceled;
    }

    /**
     * @return the batchdirectionOK
     */
    public int getBatchdirectionOK() {
        return batchdirectionOK;
    }

    /**
     * @param batchdirectionOK the batchdirectionOK to set
     */
    public void setBatchdirectionOK(int batchdirectionOK) {
        this.batchdirectionOK = batchdirectionOK;
    }

    /**
     * @return the batchdirectionNOK
     */
    public int getBatchdirectionNOK() {
        return batchdirectionNOK;
    }

    /**
     * @param batchdirectionNOK the batchdirectionNOK to set
     */
    public void setBatchdirectionNOK(int batchdirectionNOK) {
        this.batchdirectionNOK = batchdirectionNOK;
    }

    /**
     * @return the batchmaxOK
     */
    public int getBatchmaxOK() {
        return batchmaxOK;
    }

    /**
     * @param batchmaxOK the batchmaxOK to set
     */
    public void setBatchmaxOK(int batchmaxOK) {
        this.batchmaxOK = batchmaxOK;
    }

    /**
     * @return the batchmaxNOK
     */
    public int getBatchmaxNOK() {
        return batchmaxNOK;
    }

    /**
     * @param batchmaxNOK the batchmaxNOK to set
     */
    public void setBatchmaxNOK(int batchmaxNOK) {
        this.batchmaxNOK = batchmaxNOK;
    }

    /**
     * @return the batchOK
     */
    public int getBatchOK() {
        return batchOK;
    }

    /**
     * @param batchOK the batchOK to set
     */
    public void setBatchOK(int batchOK) {
        this.batchOK = batchOK;
    }

    /**
     * @return the batchNOK
     */
    public int getBatchNOK() {
        return batchNOK;
    }

    /**
     * @param batchNOK the batchNOK to set
     */
    public void setBatchNOK(int batchNOK) {
        this.batchNOK = batchNOK;
    }

    /**
     * @return the tighteningsteps
     */
    public List<TighteningStep> getTighteningsteps() {
        return tighteningsteps;
    }

    /**
     * @param tighteningsteps the tighteningsteps to set
     */
    public void setTighteningsteps(List<TighteningStep> tighteningsteps) {
        this.tighteningsteps = tighteningsteps;
    }
   
   
   }