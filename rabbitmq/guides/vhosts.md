# Virtual Hosts
In RabbitMQ, a virtual host (vhost) provides a way to segregate applications using the same RabbitMQ instance. 
It's essentially a mini RabbitMQ server within a larger RabbitMQ server, with its own set of queues, exchanges, bindings, users, and permissions.
Here are some key points about virtual hosts:
## Isolation:
Each vhost is isolated from every other vhost. This means that queues, exchanges, and bindings created in one vhost will not be visible or accessible from any other vhost.
## Permissions: 
User permissions are managed at the vhost level. This means that a user could have different permissions in different vhosts, or be allowed to access some vhosts but not others. Permissions include the ability to create and delete exchanges, create and delete queues, publish messages, and consume messages.
## Connections:
When a client application connects to RabbitMQ, it must specify the vhost that it wants to connect to. 
All actions that the client performs will be within the context of that vhost.
## Default Vhost: 
RabbitMQ comes with a default vhost named "/", although you can create additional vhosts as needed.

# Naming Virtual Hosts
Here are a few guidelines you might find useful:
## Use the application or service name: 
This makes it clear which application or service the vhost is associated with. 
For example, if you have a service named "payment", you might name the vhost "/payment".
## Include the environment: 
If you have separate vhosts for development, testing, and production environments, it can be helpful to include this in the vhost name. For example, you might have vhosts named "/payment-dev", "/payment-test", and "/payment-prod".
## Keep it lowercase: 
While RabbitMQ vhost names are case-sensitive and can include uppercase letters, it's often easier to avoid potential case confusion by sticking with lowercase.
## Avoid special characters: 
While RabbitMQ allows most characters in vhost names, some characters might have special meanings in URLs or command-line interfaces.
It's usually best to stick with alphanumeric characters, hyphens (-), and underscores (_).
## Start with a leading slash (/): 
While not required, it's common practice to start vhost names with a leading slash. 
This makes it clear that the name represents a vhost. However, do note that the default vhost in RabbitMQ is named simply "/", not "/default".

# Usage
- rabbitmqctl add_vhost myvhost
- rabbitmqctl set_permissions -p myvhost myuser ".*" ".*" ".*"
