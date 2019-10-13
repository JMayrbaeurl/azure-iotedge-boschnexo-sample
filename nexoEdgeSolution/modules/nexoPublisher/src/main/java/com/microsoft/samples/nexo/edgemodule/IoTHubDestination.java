package com.microsoft.samples.nexo.edgemodule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.samples.nexo.process.TighteningProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public final class IoTHubDestination extends AbstractPublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(IoTHubDestination.class);

    private DeviceClient deviceClient;

    private Lock twinReportLock = new ReentrantLock();

    public IoTHubDestination(MessageFactory msgFactory) {

        super(msgFactory);
    }

    @Override
    public void open() throws IOException {

        logger.debug("Opening connection to IoT Hub");

        this.deviceClient.open();
        this.deviceClient.subscribeToDeviceMethod(new DirectMethodCallback(), this, new DirectMethodStatusCallback(),
                this);
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

        this.deviceClient.sendEventAsync(message, this.createEventCallback(), message);
    }

    @Override
    public String destinationname() {
        return "IoT Hub";
    }

    @Override
    public void reportProperties(List<Property> props) throws IllegalArgumentException, IOException {

        if (props != null && props.size() > 0) {

            twinReportLock.lock();

            try {
                for (Property prop : props) {
                    this.dataCollector.setReportedProp(prop);
                }

                this.deviceClient.sendReportedProperties(this.dataCollector.getReportedProp());
            } finally {
                twinReportLock.unlock();
            }
        }
    }

    @Override
    public void uploadFile(TighteningProcess processInfo, String jsonString)
            throws IOException {
        
        byte[] raw = jsonString.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(raw);

        this.deviceClient.uploadToBlobAsync("Cycle_" + processInfo.getCycle() + ".json", 
             inputStream, raw.length, new FileUploadStatusCallback(), processInfo);
    }
}