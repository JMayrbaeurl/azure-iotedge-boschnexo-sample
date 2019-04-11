package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

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

    }

    @Override
    public String destinationname() {
        return "Edge Hub";
    }
    
}