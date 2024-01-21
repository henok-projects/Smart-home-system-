# General
Each user has a name and a password. In Galsie's case, each micro-service is a user

# RabbitMQ Management
We use a management plugin for realizing what is happening within our rabbitmq instance. 
There are different management roles (The below info is from https://www.rabbitmq.com/management.html#permissions)
## Management (permission tag: management)	
Anything the user could do via messaging protocols plus:
List virtual hosts to which they can log in via AMQP
View all queues, exchanges and bindings in "their" virtual hosts
View and close their own channels and connections
View "global" statistics covering all their virtual hosts, including activity by other users within them
## Policy Maker (permission tag: policymaker)	
Everything "management" can plus:
View, create and delete policies and parameters for virtual hosts to which they can log in via AMQP
## Monitoring (permission tag: monitoring)	
Everything "management" can plus:
- List all virtual hosts, including ones they could not access using messaging protocols
- View other users's connections and channels
- View node-level data such as memory use and clustering
- View truly global statistics for all virtual hosts
## Administrator (permission tag: administrator)	
Everything "policymaker" and "monitoring" can plus:
- Create and delete virtual hosts
- View, create and delete users
- View, create and delete permissions
- Close other users's connections

# Usage
- rabbitmqctl set_user_tags <user_name> <permission_tag>