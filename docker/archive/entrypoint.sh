#!/bin/bash
/etc/init.d/mysql start
mysql -h localhost -P 3306 -u root -proot < db_bootstrap.sql
./bootstrap_users_and_vhosts_and_run_apps.sh
echo "Done with DB and RabbitMQ bootstrap and Running Applications"