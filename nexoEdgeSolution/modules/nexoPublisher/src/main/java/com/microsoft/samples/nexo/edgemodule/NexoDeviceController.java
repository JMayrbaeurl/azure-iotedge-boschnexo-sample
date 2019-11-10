package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.samples.nexo.edgemodule.methods.ActivateMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.CallwithRawMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.DeactivateMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.GetActiveStateHandler;
import com.microsoft.samples.nexo.edgemodule.methods.ListProgramsMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.SelectProgramMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.ShowOnDisplayHandler;
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

    /**
     * 
     * @return
     */
    public boolean getLockState() {

        boolean result = true;

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                   // TODO
                }
            } finally {
                this.nexoDeviceClient.closeSession();
            }
        } else
            logger.error("Could not open communication session with nexo device");

        return result;
    }

    public boolean activateTool() {

        boolean result = false;

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                    result = this.nexoDeviceClient.activateTool();
                }
            } finally {
                this.nexoDeviceClient.closeSession();
            }
        } else
            logger.error("Could not open communication session with nexo device");

        return result;
    }

    public boolean deactivateTool() {

        boolean result = false;

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                    result = this.nexoDeviceClient.deactivateTool();
                }
            } finally {
                this.nexoDeviceClient.closeSession();
            }
        } else
            logger.error("Could not open communication session with nexo device");

        return result;
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

    /**
     * 
     * @param programNumber
     * @return
     */
    public boolean selectTighteningProgram(int programNumber) {

        boolean result = false;

        if (programNumber >= 0 && programNumber < 100 ) {
            if (this.nexoDeviceClient.openSession()) {
                try {
                    if (this.nexoDeviceClient.startCommunication()) {
                        result = this.nexoDeviceClient.selectTighteningProgram(programNumber);
                    }
                } finally {
                    this.nexoDeviceClient.closeSession();
                }
            } else
                logger.error("Could not open communication session with nexo device");
        }

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

    public SelectProgramMessageHandler createSelectProgramMessageHandler() {
        return new SelectProgramMessageHandler(this);
    }

    public GetActiveStateHandler createGetActiveStateHandler() {
        return new GetActiveStateHandler(this);
    }

    public NexoDevice getNexoDeviceClient() {
        return nexoDeviceClient;
    }

    public void setNexoDeviceClient(NexoDevice nexoDeviceClient) {
        this.nexoDeviceClient = nexoDeviceClient;
    }
    
}