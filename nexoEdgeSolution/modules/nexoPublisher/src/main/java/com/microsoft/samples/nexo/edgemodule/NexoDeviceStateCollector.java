package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;
import com.microsoft.samples.nexo.openprotocol.NexoDevice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * NexoDeviceStateCollector
 */
@Component
public class NexoDeviceStateCollector {

    private static final Logger logger = LoggerFactory.getLogger(NexoDeviceStateCollector.class);

    @Autowired
    private PublishingDestination destination;

    @Autowired
    private NexoDevice nexoDeviceClient;

    @Scheduled(fixedRate = 30000, initialDelay = 7000)
    public void doCollectStateInformation() {

        List<Property> props = new ArrayList<Property>();

        this.addBatteryLevel(props);

        if (props != null && props.size() > 0) {
            try {
                this.destination.reportProperties(props);
            } catch (IllegalArgumentException | IOException e) {
                logger.error("Exception on Twin properties reporting", e);
            }
            
            logger.debug("Reported current Nexo device state over Device Twin");
        }   
    }

    private void addBatteryLevel(List<Property> props) {

        try {
            if (this.nexoDeviceClient.startCommunication()) {
                int battLevel = this.nexoDeviceClient.getBatteryLevel();
                props.add(new Property("batterylevel", new Integer(battLevel)));
                this.nexoDeviceClient.stopCommunication();
            }
        } catch (NexoCommException ex) {
            logger.warn("Exception retrieving battery level from Nexo device.");
        }
    }

    public PublishingDestination getDestination() {
        return destination;
    }

    public void setDestination(PublishingDestination destination) {
        this.destination = destination;
    }

    public NexoDevice getNexoDeviceClient() {
        return nexoDeviceClient;
    }

    public void setNexoDeviceClient(NexoDevice nexoDeviceClient) {
        this.nexoDeviceClient = nexoDeviceClient;
    }
}