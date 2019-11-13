package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.samples.nexo.edgemodule.methods.ActivateMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.CallwithRawMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.DeactivateMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.GetActiveStateHandler;
import com.microsoft.samples.nexo.edgemodule.methods.ListProgramsMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.SelectProgramMessageHandler;
import com.microsoft.samples.nexo.edgemodule.methods.ShowOnDisplayHandler;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;
import com.microsoft.samples.nexo.openprotocol.NexoDevice;
import com.microsoft.samples.nexo.openprotocol.OpenProtocolCommands;
import com.microsoft.samples.nexo.openprotocol.PLCOutputSignalChange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * NexoDeviceController
 */
@Component
public class NexoDeviceController {

    private static final Logger logger = LoggerFactory.getLogger(NexoDeviceController.class);

    @Autowired
    private NexoDevice nexoDeviceClient;

    /**
     * 
     * @param message
     * @param duration
     * @return
     */
    public boolean showOnDisplay(final String message, int duration) { 

        boolean result = true;

        Assert.hasText(message, "Parameter 'message' must not be empty");

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                   result = this.nexoDeviceClient.showOnDisplay(message, duration);
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
     * @param rawMessage
     * @return
     */
    public String call(final String rawMessage) {

        String result = "";

        Assert.hasText(rawMessage, "Parameter 'rawMessage' must not be empty");

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication()) {
                  result = ((OpenProtocolCommands)this.nexoDeviceClient).sendROPCommand(rawMessage);
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
     * @return
     */
    public boolean getLockState() throws NexoCommException {

        boolean result = true;

        if (this.nexoDeviceClient.openSession()) {
            try {
                if (this.nexoDeviceClient.startCommunication() && this.nexoDeviceClient.subscribeToTighteningResults()) {
                    PLCOutputSignalChange signals = this.nexoDeviceClient.subscribeToOutputSignalChange();
                    if (signals != null && signals.numberOfSignals() > 0) {
                        // Hardcoded signal decoding - First is Enable/Active signal
                        result = signals.isSignalSet(0);
                    } else {
                        this.nexoDeviceClient.unsubscribeFromTighteningResults();
                        throw new NexoCommException("Could not get PLC Output signals from nexo device");
                    }

                    this.nexoDeviceClient.unsubscribeFromTighteningResults();
                }
            } finally {
                this.nexoDeviceClient.closeSession();
            }
        } else {
            logger.error("Could not open communication session with nexo device");
            throw new NexoCommException("Could not open communication session with nexo device");
        }

        return result;
    }

    /**
     * 
     * @return
     */
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