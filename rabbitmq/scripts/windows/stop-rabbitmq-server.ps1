# Get the script path
$SCRIPT_DIR = Split-Path $script:MyInvocation.MyCommand.Path

# Export the rabbitmq sbin path
. (Join-Path -Path $SCRIPT_DIR -ChildPath "export-rabbitmq.ps1")

# Configure the rabbitmq server node: these definitions will override rabbitmq defaults
$env:RABBITMQ_CONF_ENV_FILE = (Join-Path -Path $SCRIPT_DIR -ChildPath "..\..\config\rabbitmq-env.bat") # Export the rabbitmq environment paths
Write-Host "Exported environment configuration path at $env:RABBITMQ_CONF_ENV_FILE"

$nodeName = $env:NODENAME
Write-Host "Stopping the rabbitmqctl server"

# Start the server
rabbitmqctl stop $nodeName