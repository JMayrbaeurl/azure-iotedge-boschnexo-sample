package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClient;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientRegistrationCallback;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientRegistrationResult;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientStatus;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientTransportProtocol;
import com.microsoft.azure.sdk.iot.provisioning.security.SecurityProviderSymmetricKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * ProvisioningHandler
 */
public class ProvisioningHandler {

    private static Logger logger = LoggerFactory.getLogger(ProvisioningHandler.class);

    @Value("${nexopublisher_dps_scopeid:}")
    private String scopeID;

    @Value("${nexopublisher_dps_globalendpoint:global.azure-devices-provisioning.net}")
    private String dpsGlobalEndpoint;

    @Value("${nexopublisher_dps_key:}")
    private String symetricKey;

    @Value("${nexopublisher_dps_regid:}")
    private String registrationID;

    @Value("${nexopublisher_dps_timeout:10000}")
    private int maxTimeToWaitforRegistration; // in milli seconds

    @Autowired AppConfigurationManager configManager;

    public DeviceClient isAlreadyProvisioned(String protocol) throws IOException {

        DeviceClient result = null;

        ProvisioningConfiguration config = this.configManager.readProvisioningConfiguration();
        if (config != null && config.canBeUsedForIoTHubConnection()) {

            SecurityProviderSymmetricKey securityClientSymmetricKey = new SecurityProviderSymmetricKey(
                    config.getSymetricKey().getBytes(), config.getRegistrationId());
            try {
                result = DeviceClient.createFromSecurityProvider(config.getIotHubUri(), config.getDeviceId(),
                        securityClientSymmetricKey, IotHubClientProtocol.valueOf(protocol));
            } catch (URISyntaxException e) {
                logger.error("Invalid IoT Hub uri in configuration. " + e.getMessage());
			}
        }

        return result;
    }

    public DeviceClient doProvisionDevice(String protocol) throws Exception {

        DeviceClient result = null;

        ProvisioningConfiguration config = this.configManager.readProvisioningConfiguration();
        String aGlobalDPSEndpoint = this.dpsGlobalEndpoint != null ? this.dpsGlobalEndpoint : config.getDpsGlobalEndpoint();
        String aScopeId = this.scopeID != null ? this.scopeID : config.getScopeId();
        String aRegistrationId = this.registrationID != null ? this.registrationID : config.getRegistrationId();
        String aSymmetricKey = this.symetricKey != null ? this.symetricKey : config.getSymetricKey();

        SecurityProviderSymmetricKey securityClientSymmetricKey = new SecurityProviderSymmetricKey(aSymmetricKey.getBytes(), aRegistrationId);
        ProvisioningDeviceClient provisioningDeviceClient = ProvisioningDeviceClient.create(aGlobalDPSEndpoint, aScopeId, 
            ProvisioningDeviceClientTransportProtocol.HTTPS, securityClientSymmetricKey);

        try {
            ProvisioningStatus provisioningStatus = new ProvisioningStatus();
            provisioningDeviceClient.registerDevice(new ProvisioningDeviceClientRegistrationCallbackImpl(), provisioningStatus);

            while (provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getProvisioningDeviceClientStatus() != ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_ASSIGNED)
            {
                if (provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_ERROR ||
                        provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_DISABLED ||
                        provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_FAILED)
                {
                    logger.error("Registration error, bailing out", provisioningStatus.exception);
                    break;
                }

                logger.info("Waiting for Provisioning Service to register");
                Thread.sleep(this.maxTimeToWaitforRegistration);
            }

            if (provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_ASSIGNED)
            {
                logger.info("IotHUb Uri : " + provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getIothubUri());
                logger.info("Device ID : " + provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getDeviceId());

                // connect to iothub
                String iotHubUri = provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getIothubUri();
                String deviceId = provisioningStatus.provisioningDeviceClientRegistrationInfoClient.getDeviceId();
                
                result = DeviceClient.createFromSecurityProvider(iotHubUri, deviceId, securityClientSymmetricKey, IotHubClientProtocol.valueOf(protocol));

                ProvisioningConfiguration newConfig = new ProvisioningConfiguration(aRegistrationId, aSymmetricKey);
                config.setScopeId(aScopeId); config.setDpsGlobalEndpoint(aGlobalDPSEndpoint);
                config.setIotHubUri(iotHubUri); config.setDeviceId(deviceId);
                this.configManager.updateProvisioningConfiguration(newConfig);
            }
        } finally {
            if (provisioningDeviceClient != null) {
                provisioningDeviceClient.closeNow();
            }
        }

        return result;
    }

    public String getScopeID() {
        return scopeID;
    }

    public void setScopeID(String scopeID) {
        this.scopeID = scopeID;
    }

    public String getDpsGlobalEndpoint() {
        return dpsGlobalEndpoint;
    }

    public void setDpsGlobalEndpoint(String dpsGlobalEndpoint) {
        this.dpsGlobalEndpoint = dpsGlobalEndpoint;
    }

    public String getSymetricKey() {
        return symetricKey;
    }

    public void setSymetricKey(String symetricKey) {
        this.symetricKey = symetricKey;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    static class ProvisioningStatus
    {
        ProvisioningDeviceClientRegistrationResult provisioningDeviceClientRegistrationInfoClient = new ProvisioningDeviceClientRegistrationResult();
        Exception exception;
    }

    static class ProvisioningDeviceClientRegistrationCallbackImpl implements ProvisioningDeviceClientRegistrationCallback
    {
        @Override
        public void run(ProvisioningDeviceClientRegistrationResult provisioningDeviceClientRegistrationResult, Exception exception, Object context)
        {
            if (context instanceof ProvisioningStatus)
            {
                ProvisioningStatus status = (ProvisioningStatus) context;
                status.provisioningDeviceClientRegistrationInfoClient = provisioningDeviceClientRegistrationResult;
                status.exception = exception;
            }
            else
            {
                logger.error("Received unknown context");
            }
        }
    }

    public int getMaxTimeToWaitforRegistration() {
        return maxTimeToWaitforRegistration;
    }

    public void setMaxTimeToWaitforRegistration(int maxTimeToWaitforRegistration) {
        this.maxTimeToWaitforRegistration = maxTimeToWaitforRegistration;
    }
}