package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.samples.nexo.openprotocol.NexoDevice;
import com.microsoft.samples.nexo.openprotocol.OpenProtocolCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NexoDeviceController
 */
@Component
public class NexoDeviceController {

    private static final Logger logger = LoggerFactory.getLogger(NexoDeviceController.class);

    @Autowired
    private NexoDevice nexoDeviceClient;

    public boolean showOnDisplay(String message, int duration) {

        return this.nexoDeviceClient.showOnDisplay(message, duration);
    } 

    public String call(final String rawMessage) {

        return ((OpenProtocolCommands)this.nexoDeviceClient).sendROPCommand(rawMessage);
    }

    public boolean activateTool() {

        return this.nexoDeviceClient.activateTool();
    }

    public boolean deactivateTool() {

        return this.nexoDeviceClient.activateTool();
    }

    public int[] readTighteningprogramNumbers() {

        int[] result = new int[0];

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                    result = this.nexoDeviceClient.getTighteningprogramNumbers();
                    //this.nexoDeviceClient.stopCommunication();
                }
            } finally {
                this.nexoDeviceClient.closeSession();
            }
        } else
            logger.error("Could not open communication session with nexo device");

        return result;
    }

    public CallwithRawMessageHandler createCallwithRawMessageHandler() {

        return new CallwithRawMessageHandler(this);
    }

    public ShowOnDisplayHandler createShowOnDisplayHandler() {

        return new ShowOnDisplayHandler(this);
    }

    public ActivateMessageHandler createActivateMessageHandler() {

        return new ActivateMessageHandler(this);
    }

    public DeactivateMessageHandler createDeactivateMessageHandler() {

        return new DeactivateMessageHandler(this);
    }

    public ListProgramsMessageHandler createListProgramsMessageHandler() {
        return new ListProgramsMessageHandler(this);
    }

    public NexoDevice getNexoDeviceClient() {
        return nexoDeviceClient;
    }

    public void setNexoDeviceClient(NexoDevice nexoDeviceClient) {
        this.nexoDeviceClient = nexoDeviceClient;
    }
    
}