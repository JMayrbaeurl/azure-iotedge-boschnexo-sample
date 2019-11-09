package com.microsoft.samples.nexo.edgemodule;

import java.util.Arrays;

import com.microsoft.samples.nexo.openprotocol.NexoCommException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ListProgramsMessageHandler
 */
public class ListProgramsMessageHandler implements DirectMethodHandler {

    private static Logger logger = LoggerFactory.getLogger(ListProgramsMessageHandler.class);

    private final NexoDeviceController nexoDeviceController;

    public ListProgramsMessageHandler(NexoDeviceController nexoDeviceController) {
        this.nexoDeviceController = nexoDeviceController;
    }

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {
       
        logger.info("Direct method call for reading tightening program numbers");

        String result = DirectMethodHandler.STD_OK_Response;

        try {
            int[] numbers = this.nexoDeviceController.readTighteningprogramNumbers();  
            if (numbers == null || numbers.length == 0) {
                result = "[]";
            } else {
                result = Arrays.toString(numbers);
            }
        }
        catch (NexoCommException e) {
            result = DirectMethodHandler.STD_ERROR_Response;
            logger.error("Exception calling Nexo device for reading tightening program numbers. ", e);
        }

        return result;
    }

}