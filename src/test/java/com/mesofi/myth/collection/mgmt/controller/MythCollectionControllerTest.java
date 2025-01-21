package com.mesofi.myth.collection.mgmt.controller;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.fromJsonToObject;
import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadPayload;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.service.MythCollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MythCollectionController.class)
public class MythCollectionControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private MythCollectionService service; // Mock the service layer

  private final String PATH = "/figurines";

  @Test
  void createFigurine_whenMissingPayload_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/payload_missing.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenBaseNameEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/baseName_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(2)))
        .andExpect(jsonPath("$.messages").value(hasItem("baseName: must not be blank")))
        .andExpect(jsonPath("$.messages").value(hasItem("baseName: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenBaseNameTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/baseName_tooLong.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("baseName: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenBaseNameTooShort_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/baseName_tooShort.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("baseName: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenCategoryEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/category_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot coerce empty String (\"\") to `com.mesofi.myth.collection.mgmt.model.Category` value (but could if coercion was enabled using `CoercionConfig`)"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenCategoryInvalid_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/category_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `com.mesofi.myth.collection.mgmt.model.Category` from String \"A\": not one of the values accepted for Enum class: [GOLD, SPECTER, SCALE, STEEL, SILVER, SECONDARY, JUDGE, V1, V2, V3, V4, SURPLICE, ROBE, GOD, BLACK, V5]"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionChannelDistributionTooShort_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionChannel.distribution_tooShort.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionChannel.distribution: size must be between 5 and 30")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionChannelDistributionTooLong_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionChannel.distribution_tooLong.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionChannel.distribution: size must be between 5 and 30")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/distributionJPY_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(4)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.basePrice: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.releaseDate: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.preOrderDate: must not be null")))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionJPY.releaseDateConfirmed: must not be null")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYBasePriceEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/distributionJPY.basePrice_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(4)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.basePrice: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.releaseDate: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.preOrderDate: must not be null")))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionJPY.releaseDateConfirmed: must not be null")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYBasePriceInvalid_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/distributionJPY.basePrice_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `java.math.BigDecimal` from String \"x4500\": not a valid representation"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYPreOrderDateInvalid_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionJPY.preOrderDate_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"-\": Failed to deserialize java.time.LocalDate: (java.time.format.DateTimeParseException) Text '-' could not be parsed at index 1"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYReleaseDateInvalid_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionJPY.releaseDate_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"-\": Failed to deserialize java.time.LocalDate: (java.time.format.DateTimeParseException) Text '-' could not be parsed at index 1"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionJPYReleaseDateConfirmedInvalid_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionJPY.releaseDateConfirmed_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `java.lang.Boolean` from String \"trues\": only \"true\" or \"false\" recognized"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionMXNDistributorEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/distributionMXN.distributor_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionMXN.distributor.name: must not be blank")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionMXNDistributorNameTooLong_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionMXN.distributor.name_tooLong.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionMXN.distributor.name: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenDistributionMXNDistributorNameTooShort_thenReturnBadRequest()
      throws Exception {
    String payload = loadPayload("figurines/distributionMXN.distributor.name_tooShort.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("distributionMXN.distributor.name: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenLineUpEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/lineUp_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot coerce empty String (\"\") to `com.mesofi.myth.collection.mgmt.model.LineUp` value (but could if coercion was enabled using `CoercionConfig`)"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenLineUpInvalid_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/lineUp_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `com.mesofi.myth.collection.mgmt.model.LineUp` from String \"A\": not one of the values accepted for Enum class: [DDP, FIGUARTS, FIGUARTS_ZERO, MYTH_CLOTH_EX, SC_CROWN, APPENDIX, MYTH_CLOTH, SC_LEGEND]"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenEmptyPayload_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/payload_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("baseName: must not be blank")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenInvalidPath_thenReturnNotFound() throws Exception {
    String payload = loadPayload("figurines/payload_missing.json");

    mockMvc
        .perform(post("/invalid-path").contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("The requested URL was not found on this server.")))
        .andExpect(jsonPath("$.path").value("/invalid-path"));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenSuccessPayload_thenReturnCreated() throws Exception {
    String payload = loadPayload("figurines/payload_success.json");

    Figurine newFigurine = fromJsonToObject(Figurine.class, payload);

    when(service.createFigurine(newFigurine))
        .thenReturn(
            new Figurine(
                "1",
                newFigurine.getBaseName(),
                newFigurine.getDistributionJPY(),
                newFigurine.getDistributionMXN(),
                newFigurine.getTamashiiUrl(),
                newFigurine.getDistributionChannel(),
                newFigurine.getLineUp(),
                newFigurine.getSeries(),
                newFigurine.getCategory()));

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/figurines/1"))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.baseName").value("Seiya"))
        .andExpect(jsonPath("$.distributionJPY.basePrice").value(4500))
        .andExpect(jsonPath("$.distributionJPY.preOrderDate").value("2024-10-02"))
        .andExpect(jsonPath("$.distributionJPY.releaseDate").value("2024-10-02"))
        .andExpect(jsonPath("$.distributionJPY.releaseDateConfirmed").value(true))
        .andExpect(jsonPath("$.distributionMXN.distributor.id").value("1234567890"))
        .andExpect(jsonPath("$.distributionMXN.distributor.name").value("DAM"))
        .andExpect(jsonPath("$.distributionMXN.basePrice").value(4500))
        .andExpect(jsonPath("$.distributionMXN.preOrderDate").value("2024-10-02"))
        .andExpect(jsonPath("$.distributionMXN.releaseDate").value("2024-10-02"))
        .andExpect(jsonPath("$.distributionMXN.releaseDateConfirmed").value(false))
        .andExpect(jsonPath("$.tamashiiUrl").value("https://tamashiiweb.com/item/000"))
        .andExpect(jsonPath("$.distributionChannel.id").value("1234567890"))
        .andExpect(jsonPath("$.distributionChannel.distribution").value("Tamashii Store"))
        .andExpect(jsonPath("$.lineUp").value("MYTH_CLOTH_EX"))
        .andExpect(jsonPath("$.series").value("SAINT_SEIYA"))
        .andExpect(jsonPath("$.category").value("V2"));

    verify(service, times(1)).createFigurine(newFigurine);
  }

  @Test
  void createFigurine_whenSeriesEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/series_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot coerce empty String (\"\") to `com.mesofi.myth.collection.mgmt.model.Series` value (but could if coercion was enabled using `CoercionConfig`)"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenSeriesInvalid_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/series_invalid.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(
            jsonPath("$.detailMessage")
                .value(
                    "JSON parse error: Cannot deserialize value of type `com.mesofi.myth.collection.mgmt.model.Series` from String \"A\": not one of the values accepted for Enum class: [SS_THE_BEGINNING, SAINT_SEIYA, SS_OMEGA, LOST_CANVAS, SAINTIA_SHO, SOG, SS_LEGEND_OF_SANTUARY]"))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }

  @Test
  void createFigurine_whenTamashiiUrlTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/tamashiiUrl_tooLong.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("tamashiiUrl: size must be between 0 and 35")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createFigurine(any(Figurine.class));
  }
}
