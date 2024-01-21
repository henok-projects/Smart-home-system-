#!/bin/bash
(
sleep 75 # Wait for RabbitMQ to start by sleeping for 75 seconds.

USERNAMES=("galSmsService" "galEmailService" "galResourcesService" "galUsersService" "galHomesService" "galTestService")
VHOSTS=("/galsie-development" "/galsie-testing" "/galsie-production")
DEFAULT_USER_PWD="galsie123"
NODE="galsie-node1@localhost"

EXISTING_USERS=$(rabbitmqctl -n $NODE list_users)
EXISTING_VHOSTS=$(rabbitmqctl -n $NODE list_vhosts)

echo "=-=-=-=-=-=ADDING USERS=-=-=-=-=-="
for user in "${USERNAMES[@]}"; do
    if ! echo "$EXISTING_USERS" | grep -q "^$user\s"; then
        rabbitmqctl -n $NODE add_user $user $DEFAULT_USER_PWD
        echo "Added the user $user"
    else
        echo "User $user already exists, won't add"
    fi
done

echo "=-=-=-=-=-=Setting up VHOSTS=-=-=-=-=-="
for vhost in "${VHOSTS[@]}"; do
    echo "\n=-=-Checking $vhost=-=-"
    if ! echo "$EXISTING_VHOSTS" | grep -q "^$vhost$"; then
        rabbitmqctl -n $NODE add_vhost "$vhost"
        echo "Added vhost $vhost"
    else
        echo "Vhost $vhost already exists, won't add"
    fi

    for user in "${USERNAMES[@]}"; do
        rabbitmqctl -n $NODE set_permissions -p "$vhost" "$user" ".*" ".*" ".*"
        echo "\tGranted access to '$user'"
    done
done

echo "Done."
) & rabbitmq-server $@
