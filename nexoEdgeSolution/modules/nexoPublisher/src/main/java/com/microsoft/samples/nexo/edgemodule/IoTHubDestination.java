package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public final class IoTHubDestination extends AbstractPublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(IoTHubDestination.class);

    private DeviceClient deviceClient;

    private NexoTighteningDevice dataCollector = new NexoTighteningDevice();

    public IoTHubDestination(MessageFactory msgFactory) {

        super(msgFactory);
    }

    @Override
    public void open() throws IOException {

        logger.debug("Opening connection to IoT Hub");

        this.deviceClient.open();
        this.deviceClient.startDeviceTwin(new DeviceTwinStatusCallBack(), this, this.dataCollector, this);
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

    private static class DeviceTwinStatusCallBack implements IotHubEventCallback {

        @Override
        public void execute(IotHubStatusCode status, Object context) {
            logger.debug("IoT Hub responded to device twin operation with status " + status.name());
        }

    }

    @Override
    public void reportProperties(List<Property> props) throws IllegalArgumentException, IOException {

        if (props != null && props.size() > 0) {

            for (Property prop : props) {
                this.dataCollector.setReportedProp(prop);
            }
            
            this.deviceClient.sendReportedProperties(this.dataCollector.getReportedProp());
        }
    }
}