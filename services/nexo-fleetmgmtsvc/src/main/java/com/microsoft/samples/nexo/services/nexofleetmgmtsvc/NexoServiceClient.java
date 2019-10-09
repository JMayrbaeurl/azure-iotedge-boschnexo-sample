package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.sdk.iot.service.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.ServiceClient;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceMethod;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.devicetwin.MethodResult;
import com.microsoft.azure.sdk.iot.service.devicetwin.Query;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NexoServiceClient
 */
public class NexoServiceClient {

    /**
     *
     */
    public static final String STD_QUERY_FORNEXODEVICES = "SELECT * FROM devices WHERE tags.brand = 'Bosch Rexroth' AND tags.ttype = 'Nexo'";

    private static Logger logger = LoggerFactory.getLogger(NexoServiceClient.class);

    private String protocol;

    private String connectionString;

    private ServiceClient serviceClient;

    private String queryString_ForNexoDevices = STD_QUERY_FORNEXODEVICES;

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
            MethodResult result = client.invoke(deviceId, "call", Long.valueOf(10), Long.valueOf(10),
                    mapper.writeValueAsString(msg));
            response = result.getPayload().toString();
        } catch (IOException ex) {

        } catch (IotHubException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    public String getNexoDevices() {

        List<NexoDevice> devices = new ArrayList<NexoDevice>();
        NexoDeviceFactory factory = new NexoDeviceFactory();

        try {
            DeviceTwin twins = DeviceTwin.createFromConnectionString(this.connectionString);
            Query query = twins.queryTwin(this.queryString_ForNexoDevices);
            while (query.hasNext()) {
                DeviceTwinDevice nexoDevice = twins.getNextDeviceTwin(query);
                System.out.println(nexoDevice.getDeviceId());
                devices.add(factory.createFromDeviceTwin(nexoDevice));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IotHubException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[]";
        try {
            json = objectMapper.writeValueAsString(devices);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json;
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

    public String getQueryString_ForNexoDevices() {
        return queryString_ForNexoDevices;
    }

    public void setQueryString_ForNexoDevices(String queryString_ForNexoDevices) {
        this.queryString_ForNexoDevices = queryString_ForNexoDevices;
    }
}