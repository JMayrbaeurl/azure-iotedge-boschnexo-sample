package com.microsoft.samples.nexo.edgemodule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodCallback;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodData;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public abstract class AbstractPublishingDestination implements PublishingDestination {

    private static Logger logger = LoggerFactory.getLogger(AbstractPublishingDestination.class);

    protected MessageFactory messageFactory;

    protected final NexoTighteningDevice dataCollector = new NexoTighteningDevice();

    protected MessageDeliveryNotification messageDeliveryNotification;

    private Map<String, DirectMethodHandler> methodHandlers = new HashMap<String, DirectMethodHandler>();

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

    protected static class DirectMethodStatusCallback implements IotHubEventCallback {

        @Override
        public void execute(IotHubStatusCode status, Object context) {
            logger.debug("IoT Hub responded to device method operation with status " + status.name());
        }
    }

    private static final int METHOD_SUCCESS = 200;
    private static final int METHOD_NOT_DEFINED = 404;

    protected static class DirectMethodCallback implements DeviceMethodCallback {

        @Override
        public DeviceMethodData call(String methodName, Object methodData, Object context) {

            DeviceMethodData deviceMethodData;

            AbstractPublishingDestination dest = (AbstractPublishingDestination)context;
            if (dest.methodHandlers.containsKey(methodName))
            {
                DirectMethodHandler handler = dest.methodHandlers.get(methodName);
                String response = DirectMethodHandler.STD_ERROR_Response;
                if (handler != null) {
                    response = handler.handleDirectMethodCall(methodData, context);
                    logger.debug("Direct method '" + methodName + "' successfully executed. Returned: " + response);
                }

                int status = METHOD_SUCCESS;
                deviceMethodData = new DeviceMethodData(status, response);
            } else {
                int status = METHOD_NOT_DEFINED;
                deviceMethodData = new DeviceMethodData(status, "Not defined direct method " + methodName);
            }

            return deviceMethodData;
        }
    }

    @Override
    public Set<Property> getReportProperties() {
        return this.dataCollector.getReportedProp();
    }

    @Override
    public void registerDirectMethodHandler(String methodName, DirectMethodHandler handler) {

        Assert.notNull(methodName, "Parameter methodName must not be null");
        Assert.isTrue(methodName.length() > 0, "Parameter methodName must not be empty");

        Assert.notNull(handler, "Parameter handler must not be null");

        this.methodHandlers.put(methodName, handler);
    }
}