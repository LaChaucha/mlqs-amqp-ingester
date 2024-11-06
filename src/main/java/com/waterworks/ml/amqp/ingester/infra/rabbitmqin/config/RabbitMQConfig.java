package com.waterworks.ml.amqp.ingester.infra.rabbitmqin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.MultiRabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The `RabbitMQConfig` class is a Spring configuration class responsible for configuring various
 * components related to RabbitMQ message processing.
 *
 * @author Edgar Thomson
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties
public class RabbitMQConfig {

  /**
   * Configures a custom message converter for JSON messages using Jackson.
   *
   * @param mapper The Jackson `ObjectMapper` used for JSON serialization and deserialization.
   * @return A `MessageConverter` for converting JSON messages to and from Java objects.
   */
  @Bean
  public MessageConverter jsonMessageConverter(ObjectMapper mapper) {
    return new Jackson2JsonMessageConverter(mapper);
  }

  @Bean
  @ConfigurationProperties(prefix = "rabbitmq-ml")
  public CachingConnectionFactory rabbitMLConnectionFactory() {
    return new CachingConnectionFactory();
  }

  @Bean
  SimpleRoutingConnectionFactory rcf(CachingConnectionFactory rabbitMLConnectionFactory) {

    SimpleRoutingConnectionFactory rcf = new SimpleRoutingConnectionFactory();
    rcf.setDefaultTargetConnectionFactory(rabbitMLConnectionFactory);
    rcf.setTargetConnectionFactories(
        Map.of("rabbitMLConnectionFactory", rabbitMLConnectionFactory));
    return rcf;
  }

  @Bean("simpleRabbitMLListenerContainerFactory-admin")
  RabbitAdmin admin1(CachingConnectionFactory rabbitMLConnectionFactory) {
    return new RabbitAdmin(rabbitMLConnectionFactory);
  }

  @Bean
  public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
    return new RabbitListenerEndpointRegistry();
  }

  @Bean
  public RabbitListenerAnnotationBeanPostProcessor postProcessor(
      RabbitListenerEndpointRegistry registry) {
    MultiRabbitListenerAnnotationBeanPostProcessor postProcessor
        = new MultiRabbitListenerAnnotationBeanPostProcessor();
    postProcessor.setEndpointRegistry(registry);
    postProcessor.setContainerFactoryBeanName("defaultContainerFactory");
    return postProcessor;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory simpleRabbitMLListenerContainerFactory(
      CachingConnectionFactory rabbitMLConnectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(rabbitMLConnectionFactory);
    return factory;
  }

}
