package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
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
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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

    private String archiveConnectionString;

    private String archiveContainername;

    private ServiceClient serviceClient;

    private String queryString_ForNexoDevices = STD_QUERY_FORNEXODEVICES;

    /**
     * 
     * @throws IOException
     */
    public void openConnection() throws IOException {

        this.serviceClient = ServiceClient.createFromConnectionString(this.connectionString,
                IotHubServiceClientProtocol.valueOf(this.protocol));
        this.serviceClient.open();
    }

    /**
     * 
     * @param deviceId
     * @param message
     * @return
     */
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
            logger.error("Exception on calling nexo device with id '" + deviceId + "' and message '" + message + "': " + ex.getMessage());
        } catch (IotHubException e) {
            logger.error("Exception on calling nexo device with id '" + deviceId + "' and message '" + message + "': " + e.getMessage());
        }

        return response;
    }

    public boolean deactivateTool(final String deviceId) {

        boolean result = false;

        try {
            DeviceMethod client = DeviceMethod.createFromConnectionString(this.connectionString);
            MethodResult mresult = client.invoke(deviceId, "deactivate", Long.valueOf(10), Long.valueOf(10), "{}");
            String response = mresult.getPayload().toString();
            result = response.contains("true");
        } catch (IOException ex) {
            logger.error("Exception on deactivating nexo device with id '" + deviceId + "'");
        } catch (IotHubException e) {
            logger.error("Exception on deactivating nexo device with id '" + deviceId + "'");
        }

        return result;
    }

    public boolean activateTool(final String deviceId) {

        boolean result = false;

        try {
            DeviceMethod client = DeviceMethod.createFromConnectionString(this.connectionString);
            MethodResult mresult = client.invoke(deviceId, "activate", Long.valueOf(10), Long.valueOf(10), "{}");
            String response = mresult.getPayload().toString();
            result = response.contains("true");
        } catch (IOException ex) {
            logger.error("Exception on activating nexo device with id '" + deviceId + "'");
        } catch (IotHubException e) {
            logger.error("Exception on activating nexo device with id '" + deviceId + "'");
        }

        return result;
    }

    /**
     * 
     * @return
     */
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
            logger.error("Exception on getting info on all registered nexo devices. " + e.getMessage());
            e.printStackTrace();
        } catch (IotHubException e) {
            logger.error("Exception on getting info on all registered nexo devices. " + e.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[]";
        try {
            json = objectMapper.writeValueAsString(devices);
        } catch (JsonProcessingException e) {
            logger.error("Exception on getting info on all registered nexo devices. " + e.getMessage());
        }

        return json;
    }

    /**
     * 
     * @param deviceId
     * @return
     */
    public String readLatestTighteningProcessInfo(final String deviceId) {

        Assert.notNull(this.archiveConnectionString, "No Archive Azure Blob storage configured");
        Assert.notNull(deviceId, "Parameter 'deviceId' must not be null");

        String result = "{}";

        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.archiveConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(this.archiveContainername);

            if (container.exists()) {
                Date maxDate= null;
                CloudBlockBlob lastBlob = null;

                Iterable<ListBlobItem> iter = container.listBlobs(deviceId + "/");
                for (ListBlobItem blobItem : iter) {
                    if (blobItem instanceof CloudBlockBlob) {
                        Date blobDate = ((CloudBlockBlob)blobItem).getProperties().getLastModified();
                        if (maxDate == null || blobDate.after(maxDate)) {
                                maxDate = blobDate;
                                lastBlob = (CloudBlockBlob)blobItem;
                        }
                    }
                }

                if (lastBlob != null) {
                    result = lastBlob.downloadText();
                }
            }
        } catch(StorageException | URISyntaxException | InvalidKeyException | IOException ex) {
            logger.error("Exception on reading the last tightening process info for nexo '" + deviceId);
        }

        return result;
    }

    public String readPrograms(final String deviceId) {

        String result = "[]";

        try {
            DeviceMethod client = DeviceMethod.createFromConnectionString(this.connectionString);
            MethodResult mresult = client.invoke(deviceId, "programslist", Long.valueOf(10), Long.valueOf(10), "{}");
            result = mresult.getPayload().toString();
            
        } catch (IOException ex) {
            logger.error("Exception on activating nexo device with id '" + deviceId + "'");
        } catch (IotHubException e) {
            logger.error("Exception on activating nexo device with id '" + deviceId + "'");
        }

        return result;
    }

    public boolean selectProgramNumber(final String deviceId, final int programNum) {

        // TODO Implement
        return true;
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

    public NexoServiceClient(String protocol, String connectionString, String archiveConString, String archiveContname) {
        this.protocol = protocol;
        this.connectionString = connectionString;
        this.archiveConnectionString = archiveConString;
        this.archiveContainername = archiveContname;
    }

    public String getQueryString_ForNexoDevices() {
        return queryString_ForNexoDevices;
    }

    public void setQueryString_ForNexoDevices(String queryString_ForNexoDevices) {
        this.queryString_ForNexoDevices = queryString_ForNexoDevices;
    }
}