package com.mesofi.myth.collection.mgmt.config;

import com.mongodb.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class EmbeddedMongoConfig {

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "test");
  }
}
