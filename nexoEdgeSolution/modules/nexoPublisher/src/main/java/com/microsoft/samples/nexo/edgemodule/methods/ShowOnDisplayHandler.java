package com.microsoft.samples.nexo.edgemodule.methods;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.nexo.edgemodule.NexoDeviceController;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ShowOnDisplayHandler
 */
public class ShowOnDisplayHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(ShowOnDisplayHandler.class);
    
    private final NexoDeviceController nexoDeviceController;
    
    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {

        String result = DirectMethodHandler.STD_OK_Response;

        /*
         * { "message" : "Das ist ein Test", "duration" : 10 }
        */

        if (methodData != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String msgString = new String((byte[])methodData, StandardCharsets.UTF_8);
                ShowOnDisplayMessage msg = objectMapper.readValue(msgString, ShowOnDisplayMessage.class);
                result = this.nexoDeviceController.showOnDisplay(msg.getMessage(), msg.getDuration()) 
                    ? DirectMethodHandler.STD_OK_Response : DirectMethodHandler.STD_ERROR_Response;
            } catch (IOException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception on reading 'Show on display' message from direct method call", e);
			} catch (NexoCommException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception calling Nexo device for 'Show display message' command. ", e);
            }
        }
        
        return result;
    }

    public ShowOnDisplayHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }
}