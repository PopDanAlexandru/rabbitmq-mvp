package mvp.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvp.rabbitmq.config.PayloadEvent;
import mvp.rabbitmq.config.PayloadMessage;
import mvp.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String content) {
        PayloadMessage payloadMessage = new PayloadMessage(content);
        PayloadEvent payloadEvent = new PayloadEvent(payloadMessage);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, payloadEvent);
        log.info("Sent message: " + content + " with timestamp: " + payloadEvent.getTimestamp());
    }

}
