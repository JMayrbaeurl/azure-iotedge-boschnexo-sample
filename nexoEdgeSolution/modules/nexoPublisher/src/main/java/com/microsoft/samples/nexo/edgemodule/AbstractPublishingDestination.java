package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPublishingDestination implements PublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(AbstractPublishingDestination.class);

    protected MessageFactory messageFactory;

    protected AbstractPublishingDestination(MessageFactory msgFactory) {
        super();

        this.messageFactory = msgFactory;
    }

    public static class ConnectionStatusChangeCallback implements IotHubConnectionStatusChangeCallback {

        @Override
        public void execute(IotHubConnectionStatus status,
                            IotHubConnectionStatusChangeReason statusChangeReason,
                            Throwable throwable, Object callbackContext) {
            String statusStr = "Connection Status: %s";
            switch (status) {
                case CONNECTED:
                    logger.info(String.format(statusStr, "Connected"));
                    break;
                case DISCONNECTED:
                    logger.info(String.format(statusStr, "Disconnected"));
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                    System.exit(1);
                    break;
                case DISCONNECTED_RETRYING:
                    logger.info(String.format(statusStr, "Retrying"));
                    break;
                default:
                    break;
            }
        }
    }

    public static class EventCallback implements IotHubEventCallback {
        @Override
        public void execute(IotHubStatusCode status, Object context) {
            if (context instanceof Message) {
                logger.debug("Send message with status: " + status.name());
            } else {
                logger.debug("Invalid context passed");
            }
        }
    }
}