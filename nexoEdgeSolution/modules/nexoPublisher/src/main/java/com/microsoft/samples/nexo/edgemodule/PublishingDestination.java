package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.samples.nexo.process.TighteningProcess;

public interface PublishingDestination {

    public abstract void open() throws IOException;

    public abstract void sendEventAsync(Message message) throws IOException;

    public abstract void reportProperties(List<Property> props) throws IllegalArgumentException, IOException;

    public abstract Set<Property> getReportProperties();

    public abstract String destinationname();

    public abstract void registerMessageDeliveryNotification(MessageDeliveryNotification tobenotified);

    public abstract void registerDirectMethodHandler(String methodName, DirectMethodHandler handler);

    public abstract void uploadFile(TighteningProcess processInfo, String jsonString) throws IOException;
}