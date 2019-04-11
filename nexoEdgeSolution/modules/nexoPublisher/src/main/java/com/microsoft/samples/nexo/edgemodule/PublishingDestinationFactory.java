package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PublishingDestinationFactory extends AbstractFactoryBean<PublishingDestination> {

    private static Logger logger = LoggerFactory.getLogger(PublishingDestinationFactory.class);

    @Value("${nexopublisher.protocol}")
    private String protocol;

    @Value("${nexopublisher.connectionString}")
    private String connectionString;

    @Override
    protected PublishingDestination createInstance() throws Exception {
        
        PublishingDestination result = null;

        if (System.getenv("EdgeHubConnectionString") != null) {
            Assert.hasText(System.getenv("EdgeHubConnectionString"), "Env variable EdgeHubConnectionString must not be empty");
            
            Assert.notNull(this.protocol, "Property protocol must not be null");
            Assert.hasText(this.protocol, "Property protocol must not be empty");

            logger.info("Now creating device client for Edge Hub communication");
            logger.debug("Edge Hub connection string used: " + System.getenv("EdgeHubConnectionString"));
            logger.debug("Edge Hub connection protocol used: " + this.protocol);

            ModuleClient client = ModuleClient.createFromEnvironment(IotHubClientProtocol.valueOf(this.protocol));
            client.registerConnectionStatusChangeCallback(
                new AbstractPublishingDestination.ConnectionStatusChangeCallback(), null);

            EdgeHubDestination edgeHubDestination = new EdgeHubDestination();
            edgeHubDestination.setModuleClient(client);
            result = edgeHubDestination;    

        } else {
            Assert.notNull(this.connectionString, "Property connectionString must not be null");
            Assert.hasText(this.connectionString, "Property connectionString must not be empty");

            Assert.notNull(this.protocol, "Property protocol must not be null");
            Assert.hasText(this.protocol, "Property protocol must not be empty");

            logger.info("Now creating device client for IoT Hub communication");
            logger.debug("IoT Hub connection string used: " + this.connectionString);
            logger.debug("IoT Hub connection protocol used: " + this.protocol);

            DeviceClient client = new DeviceClient(this.connectionString, IotHubClientProtocol.valueOf(this.protocol));
            client.registerConnectionStatusChangeCallback(
                new AbstractPublishingDestination.ConnectionStatusChangeCallback(), null);

            IoTHubDestination ioTHubDestination = new IoTHubDestination();
            ioTHubDestination.setDeviceClient(client);
            result = ioTHubDestination;
        }

        if (result != null)
            result.open();

        logger.info("Connection to Hub established");

        return result;
    }

    @Override
    public Class<?> getObjectType() {
        return PublishingDestination.class;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the connectionString
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * @param connectionString the connectionString to set
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

}