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

Is a platform that sends messages asynchronously, from producer to consumer. The message is encoded into bytes by default.

The **Producer** sends a message to an **Exchange** server, but includes also the **Routing Key**, which is used by the Exchange to identify the correct **Queue** that will store the message (_no copy_). Is similar to how a person deposits a letter in a postal-office, together with the destination address.

The Queue registers all consumers that are connected/listening to it. So the **Consumer** waits passively for the Queue to send him the message. The Queue removes the message-copy only after the Consumer sent the confirmation that it received successfully the message.

The Exchange can route the message to only one queue, to multiple queues, or to all queues (_fanout mode_).

The Queue can be configured such that:
* is durable => the queue will survive a broker restart (messages are retained)
* is exclusive => the queue is accessible by only one consumer.
* is auto-deleted if there are no consumers connected.
* uses argument `x-message-ttl` which specifies how much time (_in milliseconds_) the message can stay in the queue, before is discarded.
* uses argument `x-max-length` which specifies the maximum nr of messages that can be stored in the queue (_if queue is full, then new messages are rejected_).

So main advantages and characteristics of RabbitMQ:
 * ensures that the message is received successfully by the consumer.
 * 

### Use-cases:
* **background processing (queue tasks)** => trigger execution of a time-consuming task, and do not wait for completion. Examples: sending emails/notifications, generating invoices/reports.
* **decouple microservices** => when we want to implement asynchronous communication between the same specific services (_Kafka uses topics and partitions, and is harder to route message to a specific service_).

### Resources:
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/3.3.5/reference/messaging/amqp.html)
