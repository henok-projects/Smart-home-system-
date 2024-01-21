# Configuration based on guide from https://www.rabbitmq.com/configure.html#config-file

# Get the folder this script is in
SCRIPT_DIR="$(dirname "$(readlink -f "$0")")"
# export rabbitmq path
source "$SCRIPT_DIR/export-rabbitmq.sh"

# Configure the rabbitmq server node: these definitions will override rabbitmq defaults
export RABBITMQ_CONF_ENV_FILE="$SCRIPT_DIR/../../config/rabbitmq-env.conf" #
echo "Exported environment configuration path at $RABBITMQ_CONF_ENV_FILE"

echo "Starting server. To stop, use another kernel and execute: rabbitmqctl stop <node-name>"
# start rabbitmq
rabbitmq-server start