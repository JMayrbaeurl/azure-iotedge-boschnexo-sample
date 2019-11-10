package com.microsoft.samples.nexo.edgemodule.methods;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.nexo.edgemodule.NexoDeviceController;
import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SelectProgramMessageHandler
 */
public class SelectProgramMessageHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(SelectProgramMessageHandler.class);

    private final NexoDeviceController nexoDeviceController;

    public SelectProgramMessageHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {

        logger.info("Direct method call for reading tightening program numbers");

        String result = DirectMethodHandler.STD_OK_Response;

        /*
         * { "programnumber" : 0 }
         */
        if (methodData != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String msgString = new String((byte[]) methodData, StandardCharsets.UTF_8);
                SelectProgramMessage msg = objectMapper.readValue(msgString, SelectProgramMessage.class);

                if (!this.nexoDeviceController.selectTighteningProgram(msg.getProgramnumber())) {
                    result = DirectMethodHandler.STD_ERROR_Response;
                    logger.error("Nexo device could not select program number '" + msg.getProgramnumber() + "'");
                }
            } catch (NexoCommException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception calling Nexo device for reading tightening program numbers. ", e);
            } catch (IOException e) {
                result = DirectMethodHandler.STD_ERROR_Response;
                logger.error("Exception on reading select program message from direct method call", e);
            }
        }

        return result;
    }
 
}