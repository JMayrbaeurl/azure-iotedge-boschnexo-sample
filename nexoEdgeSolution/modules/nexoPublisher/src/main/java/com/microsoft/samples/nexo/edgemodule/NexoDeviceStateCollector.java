package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;
import com.microsoft.samples.nexo.openprotocol.NexoDevice;
import com.microsoft.samples.nexo.openprotocol.TCPBasedNexoDevice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${nexopublisher_nexoupdates:true}")
    private boolean doSendUpdates;

    @Scheduled(fixedRate = 60000, initialDelay = 15000)
    public void doCollectStateInformation() {

        if(this.doSendUpdates) {
            List<Property> props = new ArrayList<Property>();

            if (this.nexoDeviceClient.openSession()) {
                try {
                    this.addBatteryLevel(props);
                    this.addWIFILevel(props);
                } finally {
                    this.nexoDeviceClient.closeSession();
                }
            } else
                logger.error("Could not open communication session with nexo device");

            this.addIPAddressOfNexo(props);

            if (props != null && props.size() > 0) {
                try {
                    this.destination.reportProperties(props);
                } catch (IllegalArgumentException | IOException e) {
                    logger.error("Exception on Twin properties reporting", e);
                }
                
                logger.debug("Reported current Nexo device state over Device Twin");
            } 
        }  
    }

    private void addBatteryLevel(List<Property> props) {

        try {
            if (this.nexoDeviceClient.startCommunication()) {
                int battLevel = this.nexoDeviceClient.getBatteryLevel();
                props.add(new Property("batterylevel", Integer.valueOf(battLevel)));
            }
        } catch (NexoCommException ex) {
            logger.warn("Exception retrieving battery level from Nexo device.");
        }
    }

    private void addWIFILevel(List<Property> props) {
 
         try {
            if (this.nexoDeviceClient.startCommunication()) {
                int wifiLevel = this.nexoDeviceClient.getWIFILevel();
                props.add(new Property("wifilevel", Integer.valueOf(wifiLevel)));
            }
        } catch (NexoCommException ex) {
            logger.warn("Exception retrieving wifi level from Nexo device.");
        }
   }

    private void addIPAddressOfNexo(List<Property> props) {

        if (this.nexoDeviceClient != null && this.nexoDeviceClient instanceof TCPBasedNexoDevice) {
            TCPBasedNexoDevice device = (TCPBasedNexoDevice)this.nexoDeviceClient;
            String ipAddress = device.listeningIPAddress();
            if (ipAddress != null && ipAddress.length() > 0) {
                props.add(new Property("ipaddress", ipAddress));
            }
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

    public boolean isDoSendUpdates() {
        return doSendUpdates;
    }

    public void setDoSendUpdates(boolean doSendUpdates) {
        this.doSendUpdates = doSendUpdates;
    }
}