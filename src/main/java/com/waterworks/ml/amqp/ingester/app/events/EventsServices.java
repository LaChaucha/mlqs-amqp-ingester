package com.waterworks.ml.amqp.ingester.app.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.waterworks.ml.amqp.ingester.app.events.spi.ISaveEventsRepo;
import java.io.IOException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventsServices {

  private final ISaveEventsRepo saveEventsRepo;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void processEvents(final String operation,
                            final String elementType,
                            final String origin,
                            final Object payload) {
    saveEventsRepo.saveEvent(
        String.join("_", origin, operation, elementType).toLowerCase(),
        payload
    );
  }
}
