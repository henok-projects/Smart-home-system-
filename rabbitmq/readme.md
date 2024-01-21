# RabbitMq
Message broker used for message transport between microservices.

# Reason of Choice
- Publish messages to queues & subscribe to a queue through wildcards
- Different types of queues (stream, mirror, ...)
- Configuration of queue message acknowledgements, persistence (on disk, in memory, both), ttl, size, prefetch, ..
- Certain types of queues (stream queue) can handle millions of messages
- Each RabbitMq server is a node in the RabbitMq Cluster, making it easily scalable
- Widely used
- A lot of resources available
- More reasons, read Getting_started_with_RabbitMQ_and_CloudAMQP.pdf

# Useful commands
Starting rabbit mq:
- source rabbitmq activate-rabbitmq.sh (exports paths and sets configuration files)
- rabbitmq-server start

Management plugin (https://www.rabbitmq.com/management.html) Provides ui for management and monitoring of rabbitmq
- rabbitmq-plugins enable rabbitmq_management
- rabbitmqctl add_user <name> <passcode>
- rabbitmqctl set_user_tags <user_name> <permission_tag> (see https://www.rabbitmq.com/management.html#permissions)

## Note:
Check guides/users-and-permissions.md