package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;

public interface PublishingDestination {

    public abstract void open() throws IOException;

    public abstract void sendEventAsync(Message message) throws IOException;

    public abstract String destinationname();
}