package com.microsoft.samples.nexo.edgemodule.methods;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.nexo.edgemodule.NexoDeviceController;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CallwithRawMessageHandler
 */
public class CallwithRawMessageHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(CallwithRawMessageHandler.class);
    
    private final NexoDeviceController nexoDeviceController;

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {
        
        String result = DirectMethodHandler.STD_OK_Response;

        /*
         * { "message" : "Das ist ein Test" }
        */

        if (methodData != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String msgString = new String((byte[])methodData, StandardCharsets.UTF_8);
                RawMessage msg = objectMapper.readValue(msgString, RawMessage.class);
                result = this.nexoDeviceController.call(msg.getMessage());

            } catch (IOException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception on reading raw message from direct method call", e);
			} catch (NexoCommException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception calling Nexo device for raw command. ", e);
            }
        }
        
        return result;
    }

    public CallwithRawMessageHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }
    
}