package com.microsoft.samples.nexo.edgemodule.methods;

import com.microsoft.samples.nexo.edgemodule.NexoDeviceController;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetActiveStateHandler
 */
public class GetActiveStateHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(GetActiveStateHandler.class);

    private final NexoDeviceController nexoDeviceController;

    public GetActiveStateHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {
        
        logger.info("Direct method call for getting the current lock/active state");

        String result = DirectMethodHandler.STD_OK_Response;

        try {
            boolean isActive = this.nexoDeviceController.getLockState();
            result = "{ \"lockstate\" : \"" + (isActive ? "locked" : "unlocked") + "\" }";
        }
        catch (NexoCommException e) {
            result = DirectMethodHandler.STD_ERROR_Response;
            logger.error("Exception calling Nexo device for getting lock state. ", e);
        }

        return result;
    }
}