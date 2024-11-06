package com.waterworks.ml.amqp.ingester.infra.mongodbout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.waterworks.ml.amqp.ingester.app.events.spi.ISaveEventsRepo;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class MDBIngestedAllEventsRepo implements ISaveEventsRepo {

  final MongoDatabase mongoDatabase;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void saveEvent(final String collection, final Object payload) {
    final MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

    try {
      Map<String, Object> payloadMap =
          objectMapper.readValue((byte[]) payload, new TypeReference<>() {
          });
      Document document = Document.parse(objectMapper.writeValueAsString(payloadMap));
      document.put("creationDate", Instant.now());
      mongoCollection.insertOne(document);
      log.info("Collection {} saved.", mongoCollection.getNamespace());

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
