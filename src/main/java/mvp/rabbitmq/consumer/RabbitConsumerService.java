package mvp.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import mvp.rabbitmq.config.PayloadEvent;
import mvp.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitConsumerService {

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(PayloadEvent payloadEvent) {
        long propagationTime = System.currentTimeMillis() - payloadEvent.getTimestamp();
        log.info("Received message: " + payloadEvent.getPayloadMessage().getContent());
        log.info("Message propagation time: " + propagationTime + " ms");
    }

}
