# Nexo fleetmanagement microservice

Default port: 8083

Swagger support: http://[serveraddress]:8083/swagger-ui.html

## Building

```cmd
mvn clean package dockerfile:build
```

## Running

### Run as docker container

```ps1
docker run -d -p 8083:8083 -e "server.port=8083" -e "nexoservice_connectionString=[IoT Hub connection string - with service access]" -e "nexoservice_archiveConString=[Storage connection string for file uploads]" --name nexofleetmgmt jmayrbaeurl/nexo-fleetmgmtsvc
```

### Run on Azure Container instances

```ps1
az container create --resource-group nexoPublisher --name nexo --image jmayrbaeurl/nexo-fleetmgmtsvc `
--dns-name-label nexo-demo --ports 8083 `
--log-analytics-workspace [Log Analytics ID] `
--log-analytics-workspace-key [Log Analytics Key] `
--environment-variables nexoservice_connectionString='[IoT Hub connection string - with service access]' nexoservice_archiveConString='[Storage connection string for file uploads]'
```

### Run on Azure Container instances with App insights

```ps1
az container create --resource-group nexoPublisher --name nexo --image jmayrbaeurl/nexo-fleetmgmtsvc `
--dns-name-label nexo-demo --ports 8083 `
--log-analytics-workspace [Log Analytics ID] `
--log-analytics-workspace-key [Log Analytics Key] `
--environment-variables nexoservice_connectionString='[IoT Hub connection string - with service access]' nexoservice_archiveConString='[Storage connection string for file uploads]' spring_application_name=nexofleetmgmtsvc JAVA_OPTS='-Dazure.application-insights.instrumentation-key=[App Insights Instrumentation key] -Dazure.application-insights.enabled=true -Dazure.application-insights.web.enabled=true'
```
