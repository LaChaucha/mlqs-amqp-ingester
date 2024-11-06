package com.waterworks.ml.amqp.ingester.app.events.spi;

public interface ISaveEventsRepo {
  void saveEvent(final String collection, final Object payload);
}
