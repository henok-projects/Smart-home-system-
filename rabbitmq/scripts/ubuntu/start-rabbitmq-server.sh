#!/bin/bash
# Configuration based on guide from https://www.rabbitmq.com/configure.html#config-file

CUSTOM_NODE_NAME="galsie-node1@localhost"

# Stop the RabbitMQ server
sudo systemctl stop rabbitmq-server
echo "stopped the server"

# Verify if it has been stopped
STATUS=$(sudo systemctl is-active rabbitmq-server)
echo $STATUS

echo "NODENAME=$CUSTOM_NODE_NAME" | sudo tee /etc/rabbitmq/rabbitmq-env.conf > /dev/null

# Start RabbitMQ service
sudo systemctl start rabbitmq-server

# Verify the node name
echo "Verifying the node name..."
sudo rabbitmqctl status | grep running_applications

echo "RabbitMQ setup with custom node name completed."


STATUS=$(sudo systemctl is-active rabbitmq-server)

echo $STATUS

# Get the folder this script is in
SCRIPT_DIR="$(dirname "$(readlink -f "$0")")"
echo $SCRIPT_DIR
# export rabbitmq path
source "$SCRIPT_DIR/export-rabbitmq.sh"

# Configure the rabbitmq server node: these definitions will override rabbitmq defaults
export RABBITMQ_CONF_ENV_FILE="$SCRIPT_DIR/../../config/rabbitmq-env.conf" #
echo "Exported environment configuration path at $RABBITMQ_CONF_ENV_FILE"

echo "Starting server. To stop, use another kernel and execute: rabbitmqctl stop <node-name>"


# start rabbitmq
#rabbitmq-server start -detached
sudo systemctl start rabbitmq-server

STATUS=$(sudo systemctl is-active rabbitmq-server)
echo $STATUS

TIMEOUT=300
COUNT=0
WAIT_INTERVAL=5

while  [[ "$STATUS" != "active" ]]; do
    sleep $WAIT_INTERVAL
    COUNT=$(( COUNT + WAIT_INTERVAL ))
    STATUS=$(sudo systemctl is-active rabbitmq-server)
    echo  $STATUS
done

rabbitmq-server status
echo "Done."