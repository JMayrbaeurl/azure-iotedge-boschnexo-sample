package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NexoTighteningDevice
 */
public class NexoTighteningDevice extends Device {

    private static Logger logger = LoggerFactory.getLogger(NexoTighteningDevice.class);

	@Override
	public void PropertyCall(String propertyKey, Object propertyValue, Object context) {
        
        logger.debug(propertyKey + " changed to " + propertyValue);
	}

}