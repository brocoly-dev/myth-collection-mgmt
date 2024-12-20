package com.mesofi.myth.collection.mgmt;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadPayload;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.mesofi.myth.collection.mgmt.config.EmbeddedMongoConfig;
import com.mesofi.myth.collection.mgmt.controller.MythCollectionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@Import(EmbeddedMongoConfig.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MythCollectionIT {

  @Autowired private WebTestClient webTestClient;

  // @Autowired private MongoTemplate mongoTemplate;

  @Test
  void execute_createNewFigurine() {
    String request = loadPayload("createNewFigurine/request.json");
    String response = loadPayload("createNewFigurine/response.json");

    final String uri = MythCollectionController.MAPPING;

    webTestClient
        .post()
        .uri(uri)
        .contentType(APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody()
        .json(response, JsonCompareMode.LENIENT);

    // Figurine figurine = new Figurine(null, "ss");
    // Figurine figurineSaved = mongoTemplate.save(figurine);

  }
}
