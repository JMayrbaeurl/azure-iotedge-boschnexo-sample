package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * PerformanceStatisticsMgr
 */
@Component
public class PerformanceStatisticsMgr implements MessageDeliveryNotification {

    private PerformanceStatistics stats = new PerformanceStatistics();

    private Lock updateLock = new ReentrantLock();

    private static Logger logger = LoggerFactory.getLogger(PerformanceStatisticsMgr.class);

    @Autowired
    private PublishingDestination destination;

    @Autowired
    private MessageFactory messageFactory;

    @Value("${nexopublisher_statsupdates:true}")
    private boolean doSendUpdates;

    public void incrementNumberofRequest() {

        updateLock.lock();

        stats.setNumberOfRequest(stats.getNumberOfRequest() + 1);

        updateLock.unlock();
    }

    public void incrementNumberofDeliveries() {

        updateLock.lock();

        stats.setNumberOfDeliveries(stats.getNumberOfDeliveries() + 1);

        updateLock.unlock();
    }

    public void reportCurrentStats() throws IOException {

        if (this.doSendUpdates) {
            logger.info("Reporting current performance statistics over Device Twin");

            List<Property> props = this.stats.createStatsPropertiesList();
            if (props != null && props.size() > 0)
                this.destination.reportProperties(props);
        }
    }

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void doReportCurrentStats() {

        try {
            if (this.doSendUpdates)
                this.reportCurrentStats();
        } catch (IOException e) {
            logger.error("Exception on reporting current performance statistics. " + e.getMessage());
        }
    }

    @Override
    public void messageWasSent(IotHubStatusCode status, Message message) {

        if(status == IotHubStatusCode.OK || status == IotHubStatusCode.OK_EMPTY) {

            if(message != null && this.messageFactory.isMessageForProcessInfo(message)) {

                logger.debug("Process info message successfully sent to IoT Hub");

                this.incrementNumberofDeliveries();
            }
        }
    }

    public void initializeStatsWithDeviceTwinProps() {

        boolean gotParameters = false;
        int startValueNumberOfRequests = 0;
        int startValueNumberOfDeliveries = 0;

        Set<Property> props = this.destination.getReportProperties();
        if (props != null && props.size() > 0) {
            for (Property prop : props) {
                if(prop.getKey().equals(PerformanceStatistics.NUMBER_OF_REQUEST)) {
                    gotParameters = true;
                    startValueNumberOfRequests = ((Double)prop.getValue()).intValue();
                    this.stats.setNumberOfRequest(startValueNumberOfRequests);
                } else if (prop.getKey().equals(PerformanceStatistics.NUMBER_OF_DELIVERIES)) {
                    gotParameters = true;
                    startValueNumberOfDeliveries = ((Double)prop.getValue()).intValue();
                    this.stats.setNumberOfDeliveries(startValueNumberOfDeliveries);
                }
            }
        }

        if (gotParameters) {
            logger.info("Initialized Performance statistics with " + startValueNumberOfRequests + " Number of requests and " + startValueNumberOfDeliveries + " Number of deliveries");
        }
    }

    /**
     * @return the stats
     */
    public PerformanceStatistics getStats() {
        return stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(PerformanceStatistics stats) {
        this.stats = stats;
    }

    /**
     * @return the destination
     */
    public PublishingDestination getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(PublishingDestination destination) {
        this.destination = destination;
    }

    /**
     * @return the doSendUpdates
     */
    public boolean isDoSendUpdates() {
        return doSendUpdates;
    }

    /**
     * @param doSendUpdates the doSendUpdates to set
     */
    public void setDoSendUpdates(boolean doSendUpdates) {
        this.doSendUpdates = doSendUpdates;
    }

}