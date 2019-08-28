package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PublishingDestinationFactory extends AbstractFactoryBean<PublishingDestination> {

    private static Logger logger = LoggerFactory.getLogger(PublishingDestinationFactory.class);

    @Value("${nexopublisher_protocol:MQTT}")
    private String protocol;

    @Value("${nexopublisher_connectionString:}")
    private String connectionString;

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private InputMessageHandler inputMsgHandler;

    @Value("${nexopublisher_dps_enabled:false}")
    private boolean useDeviceProvisioningService;

    @Autowired ProvisioningHandler provisioningHandler;

    @Override
    protected PublishingDestination createInstance() throws Exception {
        
        PublishingDestination result = null;

        if (System.getenv("EdgeHubConnectionString") != null || System.getenv("IOTEDGE_WORKLOADURI") != null) {
            
            Assert.notNull(this.protocol, "Property protocol must not be null");
            Assert.hasText(this.protocol, "Property protocol must not be empty");

            logger.info("Now creating device client for Edge Hub communication");
            logger.debug("Edge Hub connection protocol used: " + this.protocol);

            ModuleClient client = ModuleClient.createFromEnvironment(IotHubClientProtocol.valueOf(this.protocol));
            client.registerConnectionStatusChangeCallback(
                new AbstractPublishingDestination.ConnectionStatusChangeCallback(), null);

            Assert.notNull(this.messageFactory, "Message factory not configured");

            EdgeHubDestination edgeHubDestination = new EdgeHubDestination(this.messageFactory);
            edgeHubDestination.setModuleClient(client);
            this.inputMsgHandler.registerWithModuleClient(client, edgeHubDestination);

            result = edgeHubDestination;    

        } else {

            DeviceClient client = null;

            // Check the connection string. On null it has to be provisioned
            if (this.useDeviceProvisioningService && (this.connectionString == null || this.connectionString.length() == 0) ) {

                if (this.provisioningHandler != null) {

                    client = this.provisioningHandler.isAlreadyProvisioned(this.protocol);
                    if (client == null) {
                        client = this.provisioningHandler.doProvisionDevice(this.protocol);
                    }
                } else {
                    Assert.notNull(this.connectionString, "Property connectionString must not be null");
                    Assert.hasText(this.connectionString, "Property connectionString must not be empty");
                }
            } else {
                Assert.notNull(this.connectionString, "Property connectionString must not be null");
                Assert.hasText(this.connectionString, "Property connectionString must not be empty");

                Assert.notNull(this.protocol, "Property protocol must not be null");
                Assert.hasText(this.protocol, "Property protocol must not be empty");

                logger.info("Now creating device client for IoT Hub communication");
                logger.debug("IoT Hub connection string used: " + this.connectionString);
                logger.debug("IoT Hub connection protocol used: " + this.protocol);

                client = new DeviceClient(this.connectionString, IotHubClientProtocol.valueOf(this.protocol));
            }

            client.registerConnectionStatusChangeCallback(
                new AbstractPublishingDestination.ConnectionStatusChangeCallback(), null);

            Assert.notNull(this.messageFactory, "Message factory not configured");

            IoTHubDestination ioTHubDestination = new IoTHubDestination(this.messageFactory);
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

    /**
     * @return the messageFactory
     */
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    /**
     * @param messageFactory the messageFactory to set
     */
    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public boolean isUseDeviceProvisioningService() {
        return useDeviceProvisioningService;
    }

    public void setUseDeviceProvisioningService(boolean useDeviceProvisioningService) {
        this.useDeviceProvisioningService = useDeviceProvisioningService;
    }

}