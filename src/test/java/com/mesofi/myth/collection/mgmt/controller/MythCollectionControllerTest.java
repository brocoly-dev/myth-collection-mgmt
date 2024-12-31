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
  void createFigurine_whenInvalidPath_thenReturnNotFound() throws Exception {
    String payload = loadPayload("figurines/missing_payload.json");

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
  void createFigurine_whenMissingBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/missing_payload.json");

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
  void createFigurine_whenEmptyBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/empty_payload.json");

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
  void createFigurine_whenBaseNameTooShort_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/baseName_tooShort_field.json");

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
  void createFigurine_whenBaseNameTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/baseName_tooLong_field.json");

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
  void createFigurine_whenDistributionJPYEmpty_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/distributionJPY_empty_field.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(3)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.basePrice: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.preOrderDate: must not be null")))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distributionJPY.releaseDate: must not be null")))
        .andExpect(jsonPath("$.path").value(PATH));
  }

  @Test
  void createFigurine_whenTamashiiUrlTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("figurines/tamashiiUrl_tooLong_field.json");

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

  @Test
  void createFigurine_whenSucessPayload_thenReturnCreated() throws Exception {
    String payload = loadPayload("figurines/success_payload.json");

    Figurine newFigurine = fromJsonToObject(Figurine.class, payload);

    when(service.createFigurine(newFigurine))
        .thenReturn(
            new Figurine("1", newFigurine.getBaseName(), null, null, newFigurine.getTamashiiUrl()));

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/figurines/1"))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.baseName").value("Seiya"))
        .andExpect(jsonPath("$.tamashiiUrl").value("https://tamashiiweb.com/item/000"));

    verify(service, times(1)).createFigurine(newFigurine);
  }
}
