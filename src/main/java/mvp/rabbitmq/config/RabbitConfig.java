package mvp.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "demo-exchange";
    public static final String QUEUE_NAME = "demo-queue";
    public static final String ROUTING_KEY = "demo.routing.key";    // used to route the message from Exchange, into Queue

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME)         // Durable = the queue will survive a broker restart (messages are retained)
//                .exclusive()                          // Exclusive = the queue is accessible by only one consumer
                .autoDelete()                           // if TRUE then auto-delete the queue if there are no consumers connected
                .withArgument("x-message-ttl", 60000)   // how long the message can stay in the queue, before is discarded (milliseconds)
                .withArgument("x-max-length", 1000)     // max nr of messages that can be stored in the queue (new messages are rejected)
                .build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // configuration that allows successful serialization and deserialization
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // Set up trusted packages or class patterns
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("mvp.rabbitmq");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
