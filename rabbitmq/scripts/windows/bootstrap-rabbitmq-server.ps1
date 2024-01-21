# Get the script path
$SCRIPT_DIR = Split-Path $script:MyInvocation.MyCommand.Path

# Export the rabbitmq sbin path
. (Join-Path -Path $SCRIPT_DIR -ChildPath "export-rabbitmq.ps1")

rabbitmq-service stop # Stop the service
rabbitmq-service remove # remove it (so that the old node gets deleted)

$env:RABBITMQ_CONF_ENV_FILE = (Join-Path -Path $SCRIPT_DIR -ChildPath "..\..\config\rabbitmq-env.bat") # Export the rabbitmq environment paths
Write-Host "Exported environment configuration path at $env:RABBITMQ_CONF_ENV_FILE"

Write-Host "Installing rabbitmq service with the updated configuration server."
rabbitmq-service install
#Write-Host "Starting the rabbitmq service. Note: This doesn't start the server, just the service "
#rabbitmq-service start
Write-Host "Done. You can now start the rabbitmq server by running . .\start-rabbitmq-server.ps1"
