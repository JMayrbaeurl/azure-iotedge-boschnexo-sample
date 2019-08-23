package com.microsoft.samples.nexo.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * ConfigCommands
 */
@ShellComponent
public class ConfigCommands {

    @Autowired private NexoDeviceClient deviceClient;

    @ShellMethod("Configures the connection to the Nexo device")
    public void configConnection(String url, @ShellOption(defaultValue="4545")int port) {

         this.deviceClient.setupConnection(url, port);
    }

    @ShellMethod("Checks if the Nexo device is online, ready to control")
    public boolean isOnline() {

        return this.deviceClient.canConnect();
    }

    public NexoDeviceClient getDeviceClient() {
        return deviceClient;
    }

    public void setDeviceClient(NexoDeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }
}