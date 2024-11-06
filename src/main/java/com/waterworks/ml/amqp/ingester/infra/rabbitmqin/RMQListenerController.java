package com.waterworks.ml.amqp.ingester.infra.rabbitmqin;

import com.waterworks.ml.amqp.ingester.app.events.EventsServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RMQListenerController {

  private final EventsServices eventsServices;

  @RabbitListener(queues = "${rabbitmq-ml.queues.events}",
      containerFactory = "simpleRabbitMLListenerContainerFactory")
  public void receiveMessages(Message amqpMessage) {

    eventsServices.processEvents(
        String.valueOf(amqpMessage.getHeaders().get("operation")),
        String.valueOf(amqpMessage.getHeaders().get("elementType")),
        String.valueOf(amqpMessage.getHeaders().get("origin")),
        amqpMessage.getPayload()
    );
  }
}
