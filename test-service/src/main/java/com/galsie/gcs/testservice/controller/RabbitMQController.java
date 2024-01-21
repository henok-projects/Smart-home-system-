package com.galsie.gcs.testservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {
    /*
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("testQueue")
    Queue testQueue;

    @GetMapping("/publish")
    public ResponseEntity<?> publishToQueue(@Param("data") String data){
        rabbitTemplate.convertAndSend(TestRabbitMQConfig.TEST_EXCHANGE_NAME, TestRabbitMQConfig.TEST_QUEUE_TO_EXCHANGE_ROUTING_KEY, data);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/consume")
    public ResponseEntity<?> consumeFromQueue(){
        var message = rabbitTemplate.receive(TestRabbitMQConfig.TEST_QUEUE_NAME, 20000);

        return ResponseEntity.ok(message == null ? "NO MESSAGE" : new String(message.getBody()));
    }*/
}
