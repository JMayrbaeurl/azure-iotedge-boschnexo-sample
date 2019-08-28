package com.microsoft.samples.nexo.edgemodule;

/**
 * ProvisioningConfiguration
 */
public class ProvisioningConfiguration {

    private String scopeId;

    private String dpsGlobalEndpoint;

    private String registrationId;

    private String symetricKey;

    private String iotHubUri;

    private String deviceId;

    public boolean canBeUsedForIoTHubConnection() {

        return this.iotHubUri != null && this.deviceId != null && this.registrationId != null && this.symetricKey != null;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getDpsGlobalEndpoint() {
        return dpsGlobalEndpoint;
    }

    public void setDpsGlobalEndpoint(String dpsGlobalEndpoint) {
        this.dpsGlobalEndpoint = dpsGlobalEndpoint;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getSymetricKey() {
        return symetricKey;
    }

    public void setSymetricKey(String symetricKey) {
        this.symetricKey = symetricKey;
    }

    public String getIotHubUri() {
        return iotHubUri;
    }

    public void setIotHubUri(String iotHubUri) {
        this.iotHubUri = iotHubUri;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "ProvisioningConfiguration [deviceId=" + deviceId + ", dpsGlobalEndpoint=" + dpsGlobalEndpoint
                + ", iotHubUri=" + iotHubUri + ", registrationId=" + registrationId + ", scopeId=" + scopeId
                + ", symetricKey=" + symetricKey + "]";
    }

    public ProvisioningConfiguration(String registrationId, String symetricKey) {
        this.registrationId = registrationId;
        this.symetricKey = symetricKey;
    }

}