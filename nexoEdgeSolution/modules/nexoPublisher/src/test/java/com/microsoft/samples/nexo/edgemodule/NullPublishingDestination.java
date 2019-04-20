package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;

/**
 * NullPublishingDestination
 */
public class NullPublishingDestination extends AbstractPublishingDestination {

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
}