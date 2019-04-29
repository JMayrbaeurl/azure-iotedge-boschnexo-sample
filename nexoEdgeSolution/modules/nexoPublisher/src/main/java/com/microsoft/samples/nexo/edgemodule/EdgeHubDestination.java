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

    private String processInfoOutputName = "output1";

    private String graphEntriesOutputName = "output2";

    private String anyOutputName = "output3";

    public EdgeHubDestination(MessageFactory msgFactory) {

        super(msgFactory);
    }

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

        if (this.messageFactory.isMessageForProcessInfo(message))
            this.moduleClient.sendEventAsync(message, null, message, this.processInfoOutputName);
        else if (this.messageFactory.isMessageForGraphEntry(message))
            this.moduleClient.sendEventAsync(message, null, message, this.graphEntriesOutputName);
        else if (this.messageFactory.isMessageForAny(message))
            this.moduleClient.sendEventAsync(message, null, message, this.anyOutputName);
    }

    @Override
    public String destinationname() {
        return "Edge Hub";
    }

    /**
     * @return the processInfoOutputName
     */
    public String getProcessInfoOutputName() {
        return processInfoOutputName;
    }

    /**
     * @param processInfoOutputName the processInfoOutputName to set
     */
    public void setProcessInfoOutputName(String processInfoOutputName) {
        this.processInfoOutputName = processInfoOutputName;
    }

    /**
     * @return the graphEntriesOutputName
     */
    public String getGraphEntriesOutputName() {
        return graphEntriesOutputName;
    }

    /**
     * @param graphEntriesOutputName the graphEntriesOutputName to set
     */
    public void setGraphEntriesOutputName(String graphEntriesOutputName) {
        this.graphEntriesOutputName = graphEntriesOutputName;
    }

    /**
     * @return the anyOutputName
     */
    public String getAnyOutputName() {
        return anyOutputName;
    }

    /**
     * @param anyOutputName the anyOutputName to set
     */
    public void setAnyOutputName(String anyOutputName) {
        this.anyOutputName = anyOutputName;
    }
    
}