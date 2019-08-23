package com.microsoft.samples.nexo.cli;

import java.text.DateFormat;
import java.util.Date;

import com.microsoft.samples.nexo.openprotocol.NexoDevice;
import com.microsoft.samples.nexo.openprotocol.NexoDeviceClientFactory;

import org.springframework.stereotype.Component;

/**
 * NexoDeviceClient
 */
@Component
public class NexoDeviceClient {

    private String url;

    private int port;

    public void setupConnection(String withUrl, int onPort) {

        this.url = withUrl;
        this.port = onPort;
    }

    private NexoDevice createNexoDeviceClient() {
    
        return NexoDeviceClientFactory.createDefaultNexoDeviceClient(this.url, this.port);
    }

    public boolean isConfigured() {

        return this.url != null;
    }

    public boolean canConnect() {

        NexoDevice device = this.createNexoDeviceClient();
        boolean result = device.startCommunication();
        device.stopCommunication();
        return result;
    }

    public int batteryLevel() {

        int result = -1;

        NexoDevice device = this.createNexoDeviceClient();
        if (device.startCommunication()) {
            result = device.getBatteryLevel();
            device.stopCommunication();
        }

        return result;
    }

    public String getCurrentTime() {

        String result = "";

        NexoDevice device = this.createNexoDeviceClient();
        if (device.startCommunication()) {
            Date resultDate = device.getTime(); 
            result = DateFormat.getDateTimeInstance().format(resultDate);

            device.stopCommunication();
        }

        return result;
    }

    @Override
    public String toString() {
        return "NexoDeviceClient [port=" + port + ", url=" + url + "]";
    }
}