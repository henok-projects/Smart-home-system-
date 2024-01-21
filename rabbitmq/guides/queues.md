# Queues
In RabbitMQ, a queue is a buffer that stores messages.
These messages are sent from producers and are eventually delivered to consumers.

Queues in RabbitMQ have the following characteristics and features:
## Name:
Each queue declared gets a name.
- This could be an arbitrary string which the client chooses or an auto-generated one in case no name is provided during declaration.
- The name of the queue is the unique identifier, so if you want multiple microservices to interact with the same queue, they should all refer to the queue by the same name.
## Durable:
Durable queues remain active and keep their data intact even if RabbitMQ server restarts.
Messages in the queue must also be marked as persistent to survive a restart.
Non-durable (or transient) queues, on the other hand, lose their data when RabbitMQ restarts.
## Exclusive:
Exclusive queues can only be accessed by the connection that declared them and are deleted when that connection closes.
They are useful for temporary, private queues used by one client.
## Auto-delete:
Auto-delete queues get deleted once the last consumer unsubscribes.
They are useful for temporary queues that are no longer required.
## Bindings:
Queues are bound to one or more exchanges with a binding key.
The binding key is used by the exchange to route messages to the queue.
## Dead Letter Exchange:
When a message is rejected or expires, it can be sent to a Dead Letter Exchange (DLX) and from there to another queue.
This is useful for handling failed messages.
## Message TTL:
You can set a Time to Live (TTL) for messages. T
his means that messages will be deleted from the queue after a certain period of time, regardless of whether they have been consumed.
## Queue Length Limit:
You can set a limit to the number of ready messages a queue can contain.
If the limit is reached, the queue can either drop messages from its head, or reject publishing new messages.
## Priority Queue:
RabbitMQ supports priority queues.
This means that consumers can receive high priority messages before lower priority messages.
## Consumers:
Consumers receive messages from queues either by polling (pull API) or by subscribing to have messages pushed to them.
Multiple consumers can be associated with a single queue.
## Note:
Remember, RabbitMQ follows the AMQP 0-9-1 protocol, which means that producers send messages to exchanges, which distribute them to queues based on bindings.
Consumers then receive messages from these queues.
