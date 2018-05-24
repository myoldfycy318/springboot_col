

官方文档：https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html

Our RPC will work like this:

1.The Tut6Config will setup a new DirectExchange and a client
2.The client will leverage the convertSendAndReceive passing the exchange name, the routingKey, and the message.
3.The request is sent to an rpc_queue ("tut.rpc") queue.
4.The RPC worker (aka: server) is waiting for requests on that queue.
When a request appears, it performs the task and sends a message with the result back to the Client, using the queue from the replyTo field.
The client waits for data on the callback queue. When a message appears, it checks the correlationId property.
 If it matches the value from the request it returns the response to the application. Again, this is done automagically via the RabbitTemplate.
