package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public boolean handleDirectMethodCall(Object methodData, Object context) {

        boolean result = true;

        /*
         * { "message" : "Das ist ein Test", "duration" : 10 }
        */

        if (methodData != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String msgString = new String((byte[])methodData, StandardCharsets.UTF_8);
                ShowOnDisplayMessage msg = objectMapper.readValue(msgString, ShowOnDisplayMessage.class);
                result = this.nexoDeviceController.showOnDisplay(msg.getMessage(), msg.getDuration());
            } catch (IOException e) {
                result = false;
                logger.error("Exception on reading 'Show on display' message from direct method call", e);
			} catch (NexoCommException e) {
                result = false;
                logger.error("Exception calling Nexo device for 'Show display message' command. ", e);
            }
        }
        
        return result;
    }

    public ShowOnDisplayHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }
}