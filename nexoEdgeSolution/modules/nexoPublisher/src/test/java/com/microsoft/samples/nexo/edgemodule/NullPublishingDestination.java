package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;

/**
 * NullPublishingDestination
 */
public class NullPublishingDestination extends AbstractPublishingDestination {

    public NullPublishingDestination() {
        super(new MessageFactory());
    }

    @Override
    public void open() throws IOException {
    }

    @Override
    public void sendEventAsync(Message message) throws IOException {

    }

    @Override
    public String destinationname() {
        return "NULL";
    }

    @Override
    public void reportProperties(List<Property> props) throws IllegalArgumentException, IOException {

    }
}