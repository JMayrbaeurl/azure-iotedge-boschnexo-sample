package com.microsoft.samples.nexo.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * TimeCommands
 */
@ShellComponent
public class TimeCommands {

    private static final Logger log = LoggerFactory.getLogger(TimeCommands.class);

    @Autowired private NexoDeviceClient deviceClient;

    @ShellMethod("Returns the current time of the Nexo device")
    public String time() {

        log.info("Executing command 'time'");
        
        return this.deviceClient.getCurrentTime();
    }

    public NexoDeviceClient getDeviceClient() {
        return deviceClient;
    }

    public void setDeviceClient(NexoDeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }
}