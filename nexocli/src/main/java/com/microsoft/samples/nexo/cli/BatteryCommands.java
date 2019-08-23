package com.microsoft.samples.nexo.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * BatteryCommands
 */
@ShellComponent
public class BatteryCommands {

    private static final Logger log = LoggerFactory.getLogger(BatteryCommands.class);

    @Autowired private NexoDeviceClient deviceClient;

    @ShellMethod("Returns the current battery level of the Nexo device")
    public int batterylevel() {

        log.info("Executing command 'batterylevel'");
        
        return this.deviceClient.batteryLevel();
    }

    public Availability batterylevelAvailability() {
        return this.deviceClient != null && this.deviceClient.isConfigured()
            ? Availability.available()
            : Availability.unavailable("Connection to Nexo device not configured");
    }
    
    public NexoDeviceClient getDeviceClient() {
        return deviceClient;
    }

    public void setDeviceClient(NexoDeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }
    
}