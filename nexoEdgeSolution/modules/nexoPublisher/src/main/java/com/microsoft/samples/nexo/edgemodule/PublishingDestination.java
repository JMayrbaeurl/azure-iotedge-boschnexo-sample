package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

public interface PublishingDestination {

    public abstract void open() throws IOException;

    public abstract void sendEventAsync(Message message) throws IOException;

    public abstract void reportProperties(List<Property> props) throws IllegalArgumentException, IOException;

    public abstract String destinationname();
}