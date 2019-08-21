# Device model and Device management

## Telemetry messages

Nexo Publisher can either send full Tightening process result files, by using the 'publish' REST endpoint, or splitted single events of the several steps of the Tightening process, by using the 'stream' REST endpoint, to Azure IoT Hub. For further details see the [README](./README.md) documentation.

## Properties

Nexo Publisher supports the Azure IoT Hub Device management by using reported and desired properties of [Device Twins](https://docs.microsoft.com/en-us/azure/iot-hub/iot-hub-devguide-device-twins). Same of the properties are performance statistics of the Nexo Publisher, others are actual health or state information of the Nexo device.

### Reported by Nexo device

#### "numberOfRequest"

Positive integer as the count of Tightening process result messages that have been sent from the Nexo device to the Nexo Publisher. This counter can be reset to zero by using the command 'resetStats'. See below.

#### "numberOfDeliveries"

Positive integer as the count of Tightening process result messages that have successfully been sent from Nexo Publisher to IoT Hub. This counter can be reset to zero by using the command 'resetStats'. See below.

#### "batterylevel"

Integer value in the range of 0 and 100 for current battery level of the Nexo device. Normally this value gets updated every 30 seconds.

### Desired (set by superordinate system)

Currently none supported!

## Commands

Currently only two commands are supported. They can be used as [direct method calls over IoT Hub](https://docs.microsoft.com/en-us/azure/iot-hub/iot-hub-devguide-direct-methods) to control the Nexo Publisher component and the Nexo device.

### Commands for Nexo Publisher

The following commands be used to control the Nexo Publisher.

#### "resetStats"

Resets the statistic counters of the Nexo Publisher, that are regularily reported in the properties of the Device Twin (see above in section Properties)

### Commands for Nexo Device

The following commands are directly sent to the Nexo device to control its behaviour.

#### "showOnDisplay"

Shows message on the Nexo display. The method has two parameters. First is the message string, that will be shown on the display of the Nexo device, and second is the duration in seconds the message will be displaye. Payload sample

```json
{ "message" : "Das ist ein Test", "duration" : 10 }
```

Further details can be found in the Nexo device documentation at 7.5.3.11 "Visualization messages"
