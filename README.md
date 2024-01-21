# GCS: Galsie's Cloud Services
## Overview
- On the most basic level, Galsie's Cloud Services are required for
  - onboarding, setting up, user invitations, attesting devices, synchronizing GalAssets, ...
- On a more advanced level
  - GCS runs safety & security threat analysis
  - GCS runs some automations, routines.
  - GCS stores home data history
  - GCS performs suggestive analytics
  - GCS runs ML operations
  - ....

## Libraries
### Service-Utils library
The service utils library contains structures & utilities that make it more convenient to work on GCS. 
It holds common functionalities that is used by all services.

### Microservice-common


## Architecture
.. Image Here


## Database
### Overview
- Using a MYSQL database
### Bootstrap
- Launch terminal
    - Make sure mysql is installed, if not, figure it out.
- Run the mysql server
    - mysql.server start
- Run the bootstrap script
    - source ./db_bootstrap.sh
  
## Micro-Services
### Overview
Galsie employs the micro-service architecture for the sake of reduced coupling and simplified scalability.

### Employed Services
The microservices currently used are
  - Homes
  - Lang
  - Users

## Configuration Server
### Overview
The config server allows common configuration for clients that connect to it.
This is done through a git repository that holds:
- An application.yml file
  - Configuration stored in application.yml is used by all clients.
- A '<spring-boot-app-client-name>.yml' file
  - Configuration stored in '<spring-boot-app-client-name>.yml' is used by the service whose name is <spring-boot-app-client-name>
  - Overrides configuration in application.yml

 

## Eureka: Service Discovery

### Overview
Simply put, Eureka allows accessing a microservice by referencing a name for a microservice, rather than an IP or port.

### Eureka Server
A Server to which clients connect. The spring boot app name of the client name is used as the micro-service name.

### Eureka Client
A client that can connects to a specific Eureka server. This is when its discovered. 

## Gateway

### Overview
The Gateway allows filtered access to the different microservices through a single URL. It connects to the eureka server & provides load balanced access to the different discovered microservices

## RabbitMQ
### Overview
- Used for communicating messages between microservices in accordance with the AMPQP protocol. 






