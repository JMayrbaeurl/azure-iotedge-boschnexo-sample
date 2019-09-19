package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.sdk.iot.service.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.ServiceClient;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceMethod;
import com.microsoft.azure.sdk.iot.service.devicetwin.MethodResult;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

/**
 * NexoServiceClient
 */
public class NexoServiceClient {

    private String protocol;

    private String connectionString;

    private ServiceClient serviceClient;

    public void openConnection() throws IOException {

        this.serviceClient = ServiceClient.createFromConnectionString(this.connectionString,
                IotHubServiceClientProtocol.valueOf(this.protocol));
        this.serviceClient.open();
    }

    public String callNexo(final String deviceId, final String message) {

        String response = "";

        try {
            RawMessage msg = new RawMessage(message);
            ObjectMapper mapper = new ObjectMapper();

            DeviceMethod client = DeviceMethod.createFromConnectionString(this.connectionString);
            MethodResult result = client.invoke(deviceId, "call", Long.valueOf(10), Long.valueOf(10), mapper.writeValueAsString(msg));
            response = result.getPayload().toString();
        } catch (IOException ex) {

        } catch (IotHubException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public NexoServiceClient(String protocol, String connectionString) {
        this.protocol = protocol;
        this.connectionString = connectionString;
    }
}