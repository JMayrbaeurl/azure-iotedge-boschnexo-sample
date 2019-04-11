package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.Message;

import org.springframework.util.Assert;

public final class IoTHubDestination extends AbstractPublishingDestination {

    private DeviceClient deviceClient;

    @Override
    public void open() throws IOException {
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