#!/bin/bash
(
sleep 75 # Wait for RabbitMQ to start by sleeping for 75 seconds.

USERNAMES=("galEmailService" "galResourcesService" "galUsersService" "galHomesService" "galTestService")
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

echo "Done with RabbitMQ bootstrap."
(
nohup java -jar config-0.0.1-SNAPSHOT.jar > output.log 2>&1 &
nohup java -jar eureka-0.0.1-SNAPSHOT.jar > output1.log 2>&1 &
echo "Done with running Eureka and config."
(
sleep 30 # Wait for Eureka and config to start by sleeping for 30 seconds.
nohup java -DPORT=60131 -jar gcs-sentry-service-0.0.1-SNAPSHOT.jar > output2.log 2>&1 &
echo "Done with running GCS Sentry."
(
sleep 30 # Wait for GCS Sentry to start by sleeping for 30 seconds.
echo "30s Sleep complete. running applications."
nohup java -DPORT=60132 -jar resources-service-0.0.1-SNAPSHOT.jar > output3.log 2>&1 &
nohup java -jar continuous-service-0.0.1-SNAPSHOT.jar > output4.log 2>&1 &
nohup java -DPORT=60133 -jar homes-datawarehouse-service-0.0.1-SNAPSHOT.jar > output5.log 2>&1 &
nohup java -DPORT=60135 -jar email-service-0.0.1-SNAPSHOT.jar > output6.log 2>&1 &
nohup java -DPORT=60136 -jar users-service-0.0.1-SNAPSHOT.jar > output7.log 2>&1 &
nohup java -DPORT=60134 -jar homes-service-0.0.1-SNAPSHOT.jar > output8.log 2>&1 &
echo "Done"
)
)
)
) & rabbitmq-server $@
