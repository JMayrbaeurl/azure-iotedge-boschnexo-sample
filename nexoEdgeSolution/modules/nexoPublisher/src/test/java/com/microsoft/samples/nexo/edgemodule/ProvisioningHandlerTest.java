package com.microsoft.samples.nexo.edgemodule;

import com.microsoft.azure.sdk.iot.device.DeviceClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * ProvisioningHandlerTest
 */
@RunWith(SpringRunner.class)
public class ProvisioningHandlerTest {

    @Test
    public void testSimpleRegistration() throws Exception {

        ProvisioningHandler handler = new ProvisioningHandler();
        handler.setDpsGlobalEndpoint("global.azure-devices-provisioning.net");
        handler.setScopeID("0ne00076410");
        handler.setSymmetricKey("xIxqaPSly0oSLR6NkPbj5XRseAbIqNvPB1CnZVKlKbfbHq77Xzp7EoUm3Lg+C8SFibj1rO4n8Ls+YBeRHYSu2w==");
        handler.setRegistrationID("nexo-dps-01");
        handler.setMaxTimeToWaitforRegistration(10000);
    
        Assert.notNull(handler, "");

        DeviceClient client = handler.doProvisionDevice("MQTT");
        client.open();
        client.closeNow();
    }
}