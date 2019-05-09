package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        logger.info("Reporting current performance statistics over Device Twin");

        List<Property> props = this.stats.createStatsPropertiesList();
        if (props != null && props.size() > 0)
            this.destination.reportProperties(props);
    }

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void doReportCurrentStats() {

        try {
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

}