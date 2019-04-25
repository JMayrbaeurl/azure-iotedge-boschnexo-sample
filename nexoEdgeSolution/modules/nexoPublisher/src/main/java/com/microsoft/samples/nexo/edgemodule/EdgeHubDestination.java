package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

import org.springframework.util.Assert;

public final class EdgeHubDestination implements PublishingDestination {

    private ModuleClient moduleClient;

    @Override
    public void open() throws IOException {
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