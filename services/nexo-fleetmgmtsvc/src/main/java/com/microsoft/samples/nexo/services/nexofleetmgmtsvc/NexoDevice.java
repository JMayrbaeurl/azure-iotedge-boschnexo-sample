package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * NexoDevice
 */
public class NexoDevice {

    private String deviceID;

    public static final String TYPE_PISTOLGRIP_NUTRUNNER = "NXP";
    public static final String TYPE_RIGHTANGLE_NUTRUNNER = "NXA";
    public static final String TYPE_VARIOLINE_NUTRUNNER = "NXV";

    @JsonInclude(Include.NON_NULL)
    private String type;

    @JsonInclude(Include.NON_NULL)
    private String ipAddress;

    @JsonInclude(Include.NON_NULL)
    private Integer batterylevel;

    @JsonInclude(Include.NON_NULL)
    private Integer wifilevel;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getBatterylevel() {
        return batterylevel;
    }

    public void setBatterylevel(Integer batterylevel) {
        this.batterylevel = batterylevel;
    }

    public Integer getWifilevel() {
        return wifilevel;
    }

    public void setWifilevel(Integer wifilevel) {
        this.wifilevel = wifilevel;
    }

    @Override
    public String toString() {
        return "NexoDevice [batterylevel=" + batterylevel + ", deviceID=" + deviceID + ", ipAddress=" + ipAddress
                + ", type=" + type + ", wifilevel=" + wifilevel + "]";
    }
    
}