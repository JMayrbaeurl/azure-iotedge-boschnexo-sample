package com.microsoft.samples.nexo.edgemodule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * AppConfigurationManagerTest
 */
@RunWith(SpringRunner.class)
public class AppConfigurationManagerTest {

    @Test
    public void testWritingProvConfiFile() {
        
        AppConfigurationManager mgr = new AppConfigurationManager();
        mgr.setProvConfigFilename("nexoIoTHubConfiguration.json");
        mgr.finishCreation();

        ProvisioningConfiguration newConfig = new ProvisioningConfiguration("nexo-dps-01", "xIxqaPSly0oSLR6NkPbj5XRseAbIqNvPB1CnZVKlKbfbHq77Xzp7EoUm3Lg+C8SFibj1rO4n8Ls+YBeRHYSu2w==");
                newConfig.setScopeId("0ne00076410"); newConfig.setDpsGlobalEndpoint("global.azure-devices-provisioning.net");
        mgr.updateProvisioningConfiguration(newConfig);
    }
}