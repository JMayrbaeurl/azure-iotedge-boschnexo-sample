package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;

/**
 * MessageDeliveryNotification
 */
public interface MessageDeliveryNotification {

    public void messageWasSent(IotHubStatusCode status, Message message);
}