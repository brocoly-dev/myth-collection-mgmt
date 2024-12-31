package com.mesofi.myth.collection.mgmt.controller;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.fromJsonToObject;
import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadPayload;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.service.DistributorService;
import java.util.List;
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

  @Test
  void getAllDistributors_whenDistributorsPopulated_thenReturnAllDistributors() throws Exception {

    when(service.getAllDistributors())
        .thenReturn(List.of(new Distributor("1", "DAM"), new Distributor("2", "DTM")));

    mockMvc
        .perform(get(PATH).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].name").value("DAM"))
        .andExpect(jsonPath("$[1].id").value("2"))
        .andExpect(jsonPath("$[1].name").value("DTM"));

    verify(service, times(1)).getAllDistributors();
  }

  @Test
  void getDistributorById_whenDistributorIdInvalid_thenReturnNotFound() throws Exception {

    String invalidId = "some-invalid-id";

    when(service.getDistributorById(invalidId)).thenThrow(CatalogItemNotFoundException.class);

    mockMvc
        .perform(get(PATH + "/{invalidId}", invalidId).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("The catalog for the given identifier was not found.")))
        .andExpect(jsonPath("$.path").value("/distributors/some-invalid-id"));

    verify(service, times(1)).getDistributorById(invalidId);
  }

  @Test
  void getDistributorById_whenDistributorIdValid_thenReturnDistributor() throws Exception {

    String id = "1";

    when(service.getDistributorById(id)).thenReturn(new Distributor(id, "DTM"));

    mockMvc
        .perform(get(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("DTM"));

    verify(service, times(1)).getDistributorById(id);
  }

  @Test
  void updateDistributor_whenDistributorIdInvalid_thenReturnNotFound() throws Exception {
    String payload = loadPayload("distributors/success_payload.json");
    String invalidId = "some-invalid-id";

    Distributor existingDistributor = fromJsonToObject(Distributor.class, payload);

    when(service.updateDistributor(invalidId, existingDistributor))
        .thenThrow(CatalogItemNotFoundException.class);

    mockMvc
        .perform(
            put(PATH + "/{invalidId}", invalidId).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages")
                .value(hasItem("The catalog for the given identifier was not found.")))
        .andExpect(jsonPath("$.path").value("/distributors/some-invalid-id"));

    verify(service, times(1)).updateDistributor(invalidId, existingDistributor);
  }

  @Test
  void updateDistributor_whenDistributorIdValid_thenReturnUpdatedDistributor() throws Exception {
    String payload = loadPayload("distributors/success_payload.json");
    String id = "1";

    Distributor existingDistributor = fromJsonToObject(Distributor.class, payload);

    when(service.updateDistributor(id, existingDistributor)).thenReturn(new Distributor(id, "DTM"));

    mockMvc
        .perform(put(PATH + "/{id}", id).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("DTM"));

    verify(service, times(1)).updateDistributor(id, existingDistributor);
  }

  @Test
  void deleteDistributor_whenDistributorIdValid_thenReturnNoContent() throws Exception {
    String id = "1";

    doNothing().when(service).deleteDistributor(id);

    mockMvc
        .perform(delete(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(service, times(1)).deleteDistributor(id);
  }
}
