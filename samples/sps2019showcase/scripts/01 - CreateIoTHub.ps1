# Needed Azure command line version is 2.0.75
# Needed Azure command line extensions
# - azure-cli-iot-ext

# Dont forget to login and select the right Azure subscription
#az login
#az account set --subscription 

# Basic configuration - Resource group name to use
$nexospsgroup = "nexospsshowcase"
# Basic configuration - Name for IoT Hub
$nexospsiothubname = "nexospsiothub"
# Basic configuration - Data center location to use
$nexospsdclocation = "northeurope"

# Start by creating a resource group for the complete SPS deployment
az group create --name nexospsshowcase --location $nexospsdclocation
az configure -d group=$nexospsgroup

# Create IoT Hub
az iot hub create --name $nexospsiothubname --location northeurope --sku S1

# Register nexo-01 device
az iot hub device-identity create --device-id nexo-01 --hub-name $nexospsiothubname
az iot hub device-twin update --device-id nexo-01 --hub-name $nexospsiothubname --set tags='{\"brand\": \"Bosch Rexroth\", \"ttype\": \"Nexo\", \"nexotype\": \"NXP\"}'
az iot hub device-identity show-connection-string --device-id nexo-01 --hub-name $nexospsiothubname

# Register nexo-02 device
az iot hub device-identity create --device-id nexo-02 --hub-name $nexospsiothubname
az iot hub device-twin update --device-id nexo-02 --hub-name $nexospsiothubname --set tags='{\"brand\": \"Bosch Rexroth\", \"ttype\": \"Nexo\", \"nexotype\": \"NXP\"}'
az iot hub device-identity show-connection-string --device-id nexo-02 --hub-name $nexospsiothubname

# Register nexo-03 device
az iot hub device-identity create --device-id nexo-03 --hub-name $nexospsiothubname
az iot hub device-twin update --device-id nexo-03 --hub-name $nexospsiothubname --set tags='{\"brand\": \"Bosch Rexroth\", \"ttype\": \"Nexo\", \"nexotype\": \"NXP\"}'
az iot hub device-identity show-connection-string --device-id nexo-03 --hub-name $nexospsiothubname

# Create blob storage for file uploads and archiving
az storage account create --name nexospsdata --location northeurope --sku Standard_LRS --kind StorageV2
az storage container create --name archive --account-name nexospsdata
az storage container create --name processinfo --account-name nexospsdata
az storage container create --name '$web' --account-name nexospsdata

# Configure file uploads for IoT Hub - BUG in connection string setting
$storageconn = az storage account show-connection-string --name nexospsdata | ConvertFrom-Json
az iot hub update --name $nexospsiothubname --set properties.storageEndpoints.'$default'.connectionString=$storageconn.connectionString
az iot hub update --name $nexospsiothubname --set properties.storageEndpoints.'$default'.containerName='archive'
az iot hub update --name $nexospsiothubname --set properties.storageEndpoints.'$default'.sasTtlAsIso8601=PT1H0M0S
az iot hub update --name $nexospsiothubname --set properties.enableFileUploadNotifications=true
az iot hub update --name $nexospsiothubname --set properties.messagingEndpoints.fileNotifications.maxDeliveryCount=10
az iot hub update --name $nexospsiothubname --set properties.messagingEndpoints.fileNotifications.ttlAsIso8601=PT1H0M0S

# Configure sending all messages to processinfo
$currSubId = az account show | ConvertFrom-Json
az iot hub routing-endpoint create --hub-name $nexospsiothubname --endpoint-name nexodata --endpoint-type azurestoragecontainer `
--endpoint-resource-group $nexospsgroup --endpoint-subscription-id $currSubId.id --connection-string $storageconn.connectionString --container-name processinfo
az iot hub route create --hub-name $nexospsiothubname --endpoint-name nexodata --source-type DeviceMessages --route-name archiver --condition true --enabled true
az iot hub route create --hub-name $nexospsiothubname --endpoint-name events --source-type DeviceMessages --route-name alldata --condition true --enabled true

# Setup logging for IoT Hub
az monitor log-analytics workspace create -g $nexospsgroup -n nexo-spslogs
#az monitor diagnostic-settings create --name nexosps-iothub-logs --workspace nexo-spslogs --resource $nexospsiothubname -g $nexospsgroup --resource-type 'Microsoft.Devices/IotHubs' `
#--logs '[ { \"category\": \"Connections\", \"enabled\": true, \"retentionPolicy\": { \"days\": 0, \"enabled\": false } }, `
#      { \"category\": \"DeviceTelemetry\", \"enabled\": true, \"retentionPolicy\": { \"days\": 0, \"enabled\": false } } ]'