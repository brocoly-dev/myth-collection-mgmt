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

import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.service.DistributorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DistributorController.class)
public class DistributorControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private DistributorService service; // Mock the service layer

  private final String PATH = "/distributors";

  @Test
  void createDistributor_whenInvalidPath_thenReturnNotFound() throws Exception {
    String payload = loadPayload("distributors/missing_payload.json");

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

    verify(service, times(0)).createDistributor(any(Distributor.class));
  }

  @Test
  void createDistributor_whenMissingBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distributors/missing_payload.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributor(any(Distributor.class));
  }

  @Test
  void createDistributor_whenEmptyBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distributors/empty_payload.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("name: must not be blank")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributor(any(Distributor.class));
  }

  @Test
  void createDistributor_whenNameTooShort_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distributors/name_tooShort_field.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("name: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributor(any(Distributor.class));
  }

  @Test
  void createDistributor_whenNameTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distributors/name_tooLong_field.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("name: size must be between 3 and 20")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributor(any(Distributor.class));
  }

  @Test
  void createDistributor_whenSucessPayload_thenReturnCreated() throws Exception {
    String payload = loadPayload("distributors/success_payload.json");

    Distributor newDistributor = fromJsonToObject(Distributor.class, payload);

    when(service.createDistributor(newDistributor))
        .thenReturn(new Distributor("1", newDistributor.getName()));

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/distributors/1"))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("DTM"));

    verify(service, times(1)).createDistributor(newDistributor);
  }
}
