package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.samples.nexo.openprotocol.NexoDevice;
import com.microsoft.samples.nexo.openprotocol.OpenProtocolCommands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NexoDeviceController
 */
@Component
public class NexoDeviceController {

    @Autowired
    private NexoDevice nexoDeviceClient;

    public boolean showOnDisplay(String message, int duration) {

        return this.nexoDeviceClient.showOnDisplay(message, duration);
    } 

    public String call(final String rawMessage) {

        return ((OpenProtocolCommands)this.nexoDeviceClient).sendROPCommand(rawMessage);
    }

    public CallwithRawMessageHandler createCallwithRawMessageHandler() {

        return new CallwithRawMessageHandler(this);
    }

    public ShowOnDisplayHandler createShowOnDisplayHandler() {

        return new ShowOnDisplayHandler(this);
    }

    public NexoDevice getNexoDeviceClient() {
        return nexoDeviceClient;
    }

    public void setNexoDeviceClient(NexoDevice nexoDeviceClient) {
        this.nexoDeviceClient = nexoDeviceClient;
    }
    
}