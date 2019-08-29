package com.microsoft.samples.nexo.edgemodule;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ProvisioningConfiguration
 */
public class ProvisioningConfiguration {

    private String scopeId;

    private String dpsGlobalEndpoint;

    private String registrationId;

    private String symmetricKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String iotHubUri;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deviceId;

    public ProvisioningConfiguration() {
    }
    
    public ProvisioningConfiguration(String registrationId, String symetricKey) {
        this.registrationId = registrationId;
        this.symmetricKey = symetricKey;
    }

    public boolean canBeUsedForIoTHubConnection() {

        return this.iotHubUri != null && this.deviceId != null && this.registrationId != null && this.symmetricKey != null;
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
                + ", symetricKey=" + symmetricKey + "]";
    }

    public String getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

}