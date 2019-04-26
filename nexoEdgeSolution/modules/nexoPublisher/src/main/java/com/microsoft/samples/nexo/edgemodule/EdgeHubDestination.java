package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public final class EdgeHubDestination extends AbstractPublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(EdgeHubDestination.class);

    private ModuleClient moduleClient;

    @Override
    public void open() throws IOException {

        logger.debug("Opening connection to Edge Hub");

        this.moduleClient.open();
    }

    /**
     * @return the moduleClient
     */
    public ModuleClient getModuleClient() {
        return moduleClient;
    }

    /**
     * @param moduleClient the moduleClient to set
     */
    public void setModuleClient(ModuleClient moduleClient) {
        this.moduleClient = moduleClient;
    }

    @Override
    public void sendEventAsync(Message message) throws IOException {
        
        Assert.notNull(this.moduleClient, "Property moduleClient must not be null");

        this.moduleClient.sendEventAsync(message, null, message, "output1");
    }

    @Override
    public String destinationname() {
        return "Edge Hub";
    }
    
}