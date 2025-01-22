package com.mesofi.myth.collection.mgmt;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadPayload;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesofi.myth.collection.mgmt.controller.AnniversaryController;
import com.mesofi.myth.collection.mgmt.controller.CategoryController;
import com.mesofi.myth.collection.mgmt.controller.DistributionChannelController;
import com.mesofi.myth.collection.mgmt.controller.DistributorController;
import com.mesofi.myth.collection.mgmt.controller.LineUpController;
import com.mesofi.myth.collection.mgmt.controller.MythCollectionController;
import com.mesofi.myth.collection.mgmt.controller.SeriesController;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
public class MythCollectionIT {

  @Autowired private WebTestClient webTestClient;

  @Autowired private ObjectMapper objectMapper;

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

  @Test
  void execute_CRUD_Distributor() {
    String request = loadPayload("it/execute_CRUD_Distributor/request_DTM.json");
    String response = loadPayload("it/execute_CRUD_Distributor/response_DTM.json");

    final String uri = DistributorController.MAPPING;

    // The DTM distributor is created for the first time.
    EntityExchangeResult<byte[]> entityExchangeResult =
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
            .json(response, JsonCompareMode.LENIENT)
            .returnResult();

    HttpHeaders httpHeaders = entityExchangeResult.getResponseHeaders();
    String resourceCreated = httpHeaders.get("Location").get(0);

    request = loadPayload("it/execute_CRUD_Distributor/request_DAM.json");
    response = loadPayload("it/execute_CRUD_Distributor/response_DAM.json");

    // The DAM distributor is created next.
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

    response = loadPayload("it/execute_CRUD_Distributor/response_all.json");

    // We check if both were created.
    webTestClient
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody()
        .json(response, JsonCompareMode.LENIENT);

    // Now, we check a specific Distributor
    int index = resourceCreated.lastIndexOf("/");
    String id = resourceCreated.substring(index + 1, resourceCreated.length());

    webTestClient.get().uri(uri + "/{id}", id).exchange().expectStatus().isOk();

    request = loadPayload("it/execute_CRUD_Distributor/request_DAM_new.json");

    // Then, we update the existing Distributor

    webTestClient
        .put()
        .uri(uri + "/{id}", id)
        .contentType(APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    // Finally the existing catalog is deleted
    webTestClient.delete().uri(uri + "/{id}", id).exchange().expectStatus().isNoContent();
  }

  @Test
  void execute_CRUD_DistributionChannel() {
    String request =
        loadPayload("it/execute_CRUD_DistributionChannel/request_Tamashii_Nations.json");
    String response =
        loadPayload("it/execute_CRUD_DistributionChannel/response_Tamashii_Nations.json");

    final String uri = DistributionChannelController.MAPPING;

    // The DTM distributor is created for the first time.
    EntityExchangeResult<byte[]> entityExchangeResult =
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
            .json(response, JsonCompareMode.LENIENT)
            .returnResult();

    HttpHeaders httpHeaders = entityExchangeResult.getResponseHeaders();
    String resourceCreated = httpHeaders.get("Location").get(0);

    request = loadPayload("it/execute_CRUD_DistributionChannel/request_Stores.json");
    response = loadPayload("it/execute_CRUD_DistributionChannel/response_Stores.json");

    // The DAM distributor is created next.
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

    response = loadPayload("it/execute_CRUD_DistributionChannel/response_all.json");

    // We check if both were created.
    webTestClient
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody()
        .json(response, JsonCompareMode.LENIENT);

    // Now, we check a specific Distributor
    int index = resourceCreated.lastIndexOf("/");
    String id = resourceCreated.substring(index + 1, resourceCreated.length());

    webTestClient.get().uri(uri + "/{id}", id).exchange().expectStatus().isOk();

    request = loadPayload("it/execute_CRUD_DistributionChannel/request_Stores_new.json");

    // Then, we update the existing Distribution Channel

    webTestClient
        .put()
        .uri(uri + "/{id}", id)
        .contentType(APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    // Finally the existing catalog is deleted
    webTestClient.delete().uri(uri + "/{id}", id).exchange().expectStatus().isNoContent();
  }

  @ParameterizedTest
  @MethodSource("provideAllCatalogs")
  void execute_Read_Catalogs(String uri, String responseFile)
      throws JsonMappingException, JsonProcessingException {
    String response = loadPayload("it/execute_Read_Catalogs/" + responseFile);

    // We check if both were created.
    EntityExchangeResult<byte[]> entityExchangeResult =
        webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(APPLICATION_JSON)
            .expectBody()
            .json(response, JsonCompareMode.LENIENT)
            .returnResult();

    // Now, we check a specific item
    String responseString = new String(entityExchangeResult.getResponseBodyContent());
    List<CatalogKeyDescription> catalogKeyDescriptions =
        objectMapper.readValue(
            responseString,
            objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, CatalogKeyDescription.class));

    // We pick one of the items.
    CatalogKeyDescription catalogKeyDescription = catalogKeyDescriptions.get(0);

    webTestClient
        .get()
        .uri(uri + "/{id}", catalogKeyDescription.getKey())
        .exchange()
        .expectStatus()
        .isOk();
  }

  private static Stream<Arguments> provideAllCatalogs() {
    return Stream.of(
        Arguments.of(LineUpController.MAPPING, "/lineups/response_all.json"),
        Arguments.of(SeriesController.MAPPING, "/series/response_all.json"),
        Arguments.of(CategoryController.MAPPING, "/categories/response_all.json"),
        Arguments.of(AnniversaryController.MAPPING, "/anniversaries/response_all.json"));
  }
}
