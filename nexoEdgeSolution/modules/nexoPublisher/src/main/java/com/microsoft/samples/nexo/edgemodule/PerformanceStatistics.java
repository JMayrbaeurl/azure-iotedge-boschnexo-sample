package com.microsoft.samples.nexo.edgemodule;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

/**
 * PerformanceStatistics
 */
public class PerformanceStatistics {

    /**
     *
     */

    public static final String NUMBER_OF_DELIVERIES = "numberOfDeliveries";

    /**
     *
     */

    public static final String NUMBER_OF_REQUEST = "numberOfRequest";

    private int numberOfRequest;

    private int numberOfDeliveries;

    /**
     * @return the numberOfRequest
     */
    public int getNumberOfRequest() {
        return numberOfRequest;
    }

    /**
     * @param numberOfRequest the numberOfRequest to set
     */
    public void setNumberOfRequest(int numberOfRequest) {
        this.numberOfRequest = numberOfRequest;
    }

    /**
     * @return the numberOfDeliveries
     */
    public int getNumberOfDeliveries() {
        return numberOfDeliveries;
    }

    /**
     * @param numberOfDeliveries the numberOfDeliveries to set
     */
    public void setNumberOfDeliveries(int numberOfDeliveries) {
        this.numberOfDeliveries = numberOfDeliveries;
    }

    public List<Property> createStatsPropertiesList() {

        List<Property> result = new ArrayList<Property>();

        result.add(new Property(NUMBER_OF_REQUEST, this.numberOfRequest));
        result.add(new Property(NUMBER_OF_DELIVERIES, this.numberOfDeliveries));

        return result;
    }
}