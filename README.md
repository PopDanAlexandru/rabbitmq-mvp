# RabbitMQ
This is a Proof-of-Concept for producing and consuming RabbitMQ messages in a spring-boot app. It exposes an API through which the user produces/publishes a message into a RabbitMQ exchange, that will send further the message into a Queue, based on the configured routing key. The consumer waits passively for the message to propagate through the Queue. I added the measurement of the propagation time of each message.

---

To run this application:
1) Install RabbitMQ: https://www.rabbitmq.com/docs/download
2) Run the `.exe` file to start the RabbitMQ server.
4) Build the project by executing `mvn clean install`.
6) Start `RabbitmqMvpApplication`.
7) Call the exposed API (_this is the request in cURL format_):
```
curl --location 'http://localhost:8080/produce?message=test-notification'
```

![RabbitMQ components.png](RabbitMQ%20components.png)

---

# RabbitMQ concepts

Is a platform that sends messages asynchronously, from producer to consumer, using [AMQP protocol](https://docs.spring.io/spring-boot/3.3.5/reference/messaging/amqp.html). The message is encoded into bytes by default.

The **Producer** sends a message to an **Exchange** server, but includes also the **Routing Key**, which is used by the Exchange to identify the correct **Queue** that will store the message. Is similar to how a person deposits a letter in a postal-office, together with the destination address.

The Queue registers all consumers that are connected/listening to it. So the **Consumer** waits passively for the Queue to send him the message. The Queue removes the message-copy only after the Consumer sent the acknowledge to confirm that it received successfully the message. So by default, a consumer cannot retrieve a message that was already fetched/processed, without configuring a longer retention period.

The Exchange can route the message to only one queue, or to multiple queues (_fanout mode_).

The Queue can be configured such that:
* is durable => the queue will survive a broker restart (_existing messages are not lost_).
* is exclusive => the queue is accessible by only one consumer.
* is auto-deleted if there are no consumers connected.
* uses argument `x-message-ttl` which specifies how much time (_in milliseconds_) the message can stay in the queue, before is discarded.
* uses argument `x-max-length` which specifies the maximum nr of messages that can be stored in the queue (_if queue is full, then new messages are rejected_).
* if the consumer cannot process the message and does not acknowledge it successfully, then the queue marks the message as a "dead-letter" and re-publishes that message into a dead-letter-queue.
* if the queue is full, then it can reject new messages or can move older messages into a dead-letter-queue.

### Use-cases and advantages:
* **Background processing** (_queuing tasks_) => trigger reliable execution of a time-consuming task that should be processed occasionally, and do not wait for completion  (_to deliver a message once, without the need to persist it_). But is **reliable** because we can ensure that the message/order was received successfully by the consumer, so the process truly started (_while Kafka consumers do not acknowledge the successful receival/processing of messages_). **Examples**: sending emails/notifications, generating invoices/reports.
* to **decouple microservices** => when we want to implement asynchronous communication between the same specific services and complex routing (_Kafka consumers use the principle of first-come first-served, and uses topics and partitions, and is harder to route message to a specific consumer_).
* To configure messages that have **higher priority**, to be propagated faster through the queue. For example, when an issue occurs, and the message that triggers the fix must be executed faster. (_while Kafka does not allow priority_)

### Resources:
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/3.3.5/reference/messaging/amqp.html)
