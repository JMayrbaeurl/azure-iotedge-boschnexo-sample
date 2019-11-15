az container delete --resource-group nexoPublisher --name nexo

az container create --resource-group nexospsshowcase --name nexofleetmgmtsvc --image jmayrbaeurl/nexo-fleetmgmtsvc `
--dns-name-label nexo-demo --ports 8083 `
--log-analytics-workspace [Enter your workspace id here] `
--log-analytics-workspace-key [Enter your workspace key here] `
--environment-variables nexoservice_connectionString='[Iot hub service connection string]' nexoservice_archiveConString='[archive connection string]'
