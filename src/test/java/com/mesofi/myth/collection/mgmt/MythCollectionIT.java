package com.mesofi.myth.collection.mgmt;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadPayload;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.mesofi.myth.collection.mgmt.controller.MythCollectionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MythCollectionIT {

  @Autowired private WebTestClient webTestClient;

  @Test
  void execute_createNewFigurine() {
    String request = loadPayload("it/createNewFigurine/request.json");
    String response = loadPayload("it/createNewFigurine/response.json");

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
  }
}
