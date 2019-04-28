package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public final class IoTHubDestination extends AbstractPublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(IoTHubDestination.class);

    private DeviceClient deviceClient;

    public IoTHubDestination(MessageFactory msgFactory) {

        super(msgFactory);
    }

    @Override
    public void open() throws IOException {

        logger.debug("Opening connection to IoT Hub");

        this.deviceClient.open();
    }

    /**
     * @return the deviceClient
     */
    public DeviceClient getDeviceClient() {
        return deviceClient;
    }

    /**
     * @param deviceClient the deviceClient to set
     */
    public void setDeviceClient(DeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }

    @Override
    public void sendEventAsync(Message message) throws IOException {

        Assert.notNull(this.deviceClient, "Property deviceClient must not be null");

        this.deviceClient.sendEventAsync(message, null, null);
    }

    @Override
    public String destinationname() {
        return "IoT Hub";
    }

}