package com.microsoft.samples.nexo.edgemodule;

import java.util.List;
import java.util.Set;

import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPublishingDestination implements PublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(AbstractPublishingDestination.class);

    protected MessageFactory messageFactory;

    protected final NexoTighteningDevice dataCollector = new NexoTighteningDevice();

    protected MessageDeliveryNotification messageDeliveryNotification;

    protected AbstractPublishingDestination(MessageFactory msgFactory) {
        super();

        this.messageFactory = msgFactory;
    }

    public void registerMessageDeliveryNotification(MessageDeliveryNotification tobenotified) {

        this.messageDeliveryNotification = tobenotified;
    }

    public static class ConnectionStatusChangeCallback implements IotHubConnectionStatusChangeCallback {

        @Override
        public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason,
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

    public EventCallback createEventCallback() {

        if (this.messageDeliveryNotification != null)
            return new EventCallback(this.messageDeliveryNotification);
        else
            return null;
    }

    public EventCallback createEventCallback(MessageDeliveryNotification tobeNotified) {

        return new EventCallback(tobeNotified);
    }

    public static class EventCallback implements IotHubEventCallback {

        public EventCallback(MessageDeliveryNotification tobeNotified) {
            this.messageDeliveryNotification = tobeNotified;
        }

        private final MessageDeliveryNotification messageDeliveryNotification;

        @Override
        public void execute(IotHubStatusCode status, Object context) {
            if (context instanceof Message) {
                logger.debug("Send message with status: " + status.name());
                if (this.messageDeliveryNotification != null)
                    this.messageDeliveryNotification.messageWasSent(status, (Message) context);
            } else {
                logger.debug("Invalid context passed");
            }
        }
    }

    protected static class DeviceTwinStatusCallBack implements IotHubEventCallback {

        @Override
        public void execute(IotHubStatusCode status, Object context) {
            logger.debug("IoT Hub responded to device twin operation with status " + status.name());
        }

    }

    @Override
    public Set<Property> getReportProperties() {
        return this.dataCollector.getReportedProp();
    }
}