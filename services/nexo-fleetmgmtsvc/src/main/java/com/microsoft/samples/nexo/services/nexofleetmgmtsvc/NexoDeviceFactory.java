package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.util.Iterator;
import java.util.Set;

import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.devicetwin.Pair;

import org.springframework.util.Assert;

/**
 * NexoDeviceFactory
 */
public class NexoDeviceFactory {

    public NexoDevice createFromDeviceTwin(final DeviceTwinDevice nexoDevice) {
        
        Assert.notNull(nexoDevice, "Parameter for nexoDevice must not be null");

        NexoDevice result = new NexoDevice();
        result.setDeviceID(nexoDevice.getDeviceId());

        Set<Pair> tags = nexoDevice.getTags();
        if (tags != null && !tags.isEmpty()) {
            Iterator<Pair> iter = tags.iterator();
            while(iter.hasNext()) {
                Pair tag = iter.next();
                if ("nexotype".equals(tag.getKey())) {
                    result.setType(tag.getValue().toString());
                }
            }
        }

        Set<Pair> props = nexoDevice.getReportedProperties();
        if (props != null && !props.isEmpty()) {
            Iterator<Pair> iter = props.iterator();
            while(iter.hasNext()) {
                Pair prop = iter.next();
                if ("batterylevel".equals(prop.getKey())) {
                    result.setBatterylevel(Integer.valueOf(((Double)prop.getValue()).intValue()));
                }
            }
        }

        return result;
    }
}