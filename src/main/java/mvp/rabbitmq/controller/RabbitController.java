package mvp.rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvp.rabbitmq.producer.RabbitProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RabbitController {

    private final RabbitProducerService producerService;

    @GetMapping("/produce")
    public String produceMessage(@RequestParam("message") String message) {
        producerService.sendMessage(message);
        return "Message sent to RabbitMQ: " + message;
    }

}
