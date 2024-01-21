


# Exchanges

## Direct Exchange:
This type of exchange delivers messages to queues based on the message routing key.
- The routing key must exactly match the binding key of the queue.
- It's suitable when you want a message to be consumed by only specific queues.

## Fanout Exchange:
This exchange routes messages to all the queues that are bound to it.
- The routing key is ignored in this case. 
- If N queues are bound to a fanout exchange, when a new message is published to that exchange a copy of the message is delivered to all N queues.
- It's ideal for the broadcast routing of messages.

## Topic Exchange:
This exchange routes messages to queues based on the pattern of the routing key.
- The routing key is a list of words, delimited by a dot (e.g., "word1.word2.word3").
- The binding key must also be in the same form. The special characters "*" and "#" can be used in the binding key to represent a single word, and zero or more words, respectively.
- It's suitable when you want the message to be routed based on multiple criteria.

## Headers Exchange:
This exchange uses message header attributes for routing.
- The routing key is ignored. Instead, the attributes defined in the headers property of the message are used to route the message.
- It's useful when you want more flexibility in your routing mechanism; however, it can be slower compared to other exchanges due to the overhead of handling message headers.
