# Bootstrapping RabbitMQ
### Mac
1. Open {path}/GalCloud/rabbitmq/scripts/mac/export-rabbitmq.sh 
   - Change the path of the rabbitmq to that in your local PC
2. Launch terminal and cd into {path}/GalCould/rabbitmq/scripts/mac, and run 
   - . start-rabbitmq-server.sh
   - . bootstrap-users-and-vhosts (you may have to do that from another terminal)
   
### Ubuntu
1. Open {path}/GalCloud/rabbitmq/scripts/ubuntu/export-rabbitmq.sh
   - Change the path of the rabbitmq to that in your local PC. You can do this by searching for the rabbmitmqctl on your
      Linux OS, the path should be similar to the one found in the file
2. Launch terminal and cd into {path}/GalCould/rabbitmq/scripts/ubuntu, and run
   - . start-rabbitmq-server.sh
   - . bootstrap-users-and-vhosts (you may have to do that from another terminal)

### Windows
1. Open rabbitmq/scripts/windows/export-rabbitmq.ps1
   - Change the path of the rabbitmq sbin to that in your local PC
2. Launch windows powershell as administrator. Execute the following commands to configure rabbitmq
   - cd {path}/GalCloud/rabbitmq/scripts/windows
   - . .\bootstrap-rabbitmq-server.ps1
   - . .\start-rabbitmq-server.ps1
   - . .\bootstrap-users-and-vhosts.ps1 (you may have to do that form another powershell window)

# Starting the RabbitMQ Server
### MAC
1. Launch terminal and cd into {path}/GalCould/rabbitmq/scripts/mac, and run
   - . start-rabbitmq-server.sh

### Ubuntu
1. Launch terminal and cd into {path}/GalCould/rabbitmq/scripts/ubuntu, and run
   - . start-rabbitmq-server.sh

### Windows
1. Launch windows powershell as administrator.
2. cd {path}/GalCloud/rabbitmq/scripts/windows
   - . .\start-rabbitmq-server.ps1
3. NOTE: If failed, stop the rabbitmq service (by exporting rabbitmq then running rabbitmq-service stop), then run the script again

# Stopping the RabbitMQ Server
### Windows
### MAC
1. Launch windows powershell as administrator.
2. cd {path}/GalCloud/rabbitmq/scripts/windows
   - . .\stop-rabbitmq-server.ps1
