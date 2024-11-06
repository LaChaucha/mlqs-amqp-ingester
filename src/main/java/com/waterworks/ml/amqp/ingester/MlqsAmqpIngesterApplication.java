package com.waterworks.ml.amqp.ingester;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * The `M3ServicesApplication` class serves as the entry point for the Spring Boot application
 * responsible for managing and executing jobs. It is annotated with `@SpringBootApplication`, which
 * indicates that this class is the primary configuration class for the Spring Boot application.
 * This class provides the foundation for configuring and running the Spring Boot application for
 * job management.
 *
 * @author Edgar Thomson
 * @version 1.0
 */
//@SpringBootApplication
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
@EnableEncryptableProperties
public class MlqsAmqpIngesterApplication {

  /**
   * The main method is the entry point for the application. It initializes and starts the Spring
   * Boot application.
   *
   * @param args Command-line arguments that can be passed when launching the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(MlqsAmqpIngesterApplication.class, args);
  }
}
