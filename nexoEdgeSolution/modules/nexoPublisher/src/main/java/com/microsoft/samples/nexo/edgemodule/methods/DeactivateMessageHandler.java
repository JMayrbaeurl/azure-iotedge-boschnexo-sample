package com.microsoft.samples.nexo.edgemodule.methods;

import com.microsoft.samples.nexo.edgemodule.NexoDeviceController;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DeactivateMessageHandler
 */
public class DeactivateMessageHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(DeactivateMessageHandler.class);

    private final NexoDeviceController nexoDeviceController;

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {
        
        String result = DirectMethodHandler.STD_OK_Response;

        try {
            boolean cresult = this.nexoDeviceController.deactivateTool();
            if (!cresult)
                result = DirectMethodHandler.STD_ERROR_Response;
        } catch (NexoCommException e) {
            result = DirectMethodHandler.STD_ERROR_Response;
            logger.error("Exception calling deactivate Tool command. ", e);
        }

        return result;
    }

    public DeactivateMessageHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }
}