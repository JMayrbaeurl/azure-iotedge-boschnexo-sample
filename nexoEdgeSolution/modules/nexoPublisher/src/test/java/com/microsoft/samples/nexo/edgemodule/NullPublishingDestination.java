package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        String converted = new String(message.getBytes(), StandardCharsets.UTF_8);
        //System.out.println(converted);
    }

    @Override
    public String destinationname() {
        return "NULL";
    }
}