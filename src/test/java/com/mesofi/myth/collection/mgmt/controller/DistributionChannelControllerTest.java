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
import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.service.DistributionChannelService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DistributionChannelController.class)
public class DistributionChannelControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private DistributionChannelService service; // Mock the service layer

  private final String PATH = "/distribution-channels";

  @Test
  void createDistributionChannel_whenInvalidPath_thenReturnNotFound() throws Exception {
    String payload = loadPayload("distribution-channels/payload_missing.json");

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

    verify(service, times(0)).createDistributionChannel(any(DistributionChannel.class));
  }

  @Test
  void createDistributionChannel_whenMissingBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distribution-channels/payload_missing.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("The required request body is missing.")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributionChannel(any(DistributionChannel.class));
  }

  @Test
  void createDistributionChannel_whenEmptyBody_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distribution-channels/payload_empty.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(jsonPath("$.messages").value(hasItem("distribution: must not be blank")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributionChannel(any(DistributionChannel.class));
  }

  @Test
  void createDistributionChannel_whenNameTooShort_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distribution-channels/distribution_tooShort.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distribution: size must be between 5 and 30")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributionChannel(any(DistributionChannel.class));
  }

  @Test
  void createDistributionChannel_whenNameTooLong_thenReturnBadRequest() throws Exception {
    String payload = loadPayload("distribution-channels/distribution_tooLong.json");

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.messages").isArray())
        .andExpect(jsonPath("$.messages", hasSize(1)))
        .andExpect(
            jsonPath("$.messages").value(hasItem("distribution: size must be between 5 and 30")))
        .andExpect(jsonPath("$.path").value(PATH));

    verify(service, times(0)).createDistributionChannel(any(DistributionChannel.class));
  }

  @Test
  void createDistributionChannel_whenSucessPayload_thenReturnCreated() throws Exception {
    String payload = loadPayload("distribution-channels/payload_success.json");

    DistributionChannel newDistributionChannel =
        fromJsonToObject(DistributionChannel.class, payload);

    when(service.createDistributionChannel(newDistributionChannel))
        .thenReturn(new DistributionChannel("1", newDistributionChannel.getDistribution()));

    mockMvc
        .perform(post(PATH).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/distribution-channels/1"))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.distribution").value("Stores"));

    verify(service, times(1)).createDistributionChannel(newDistributionChannel);
  }

  @Test
  void getAllDistributionChannel_whenDistributionChannelPopulated_thenReturnAllDistributionChannel()
      throws Exception {

    when(service.getAllDistributionChannels())
        .thenReturn(
            List.of(new DistributionChannel("1", "Stores"), new DistributionChannel("2", "Web")));

    mockMvc
        .perform(get(PATH).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].distribution").value("Stores"))
        .andExpect(jsonPath("$[1].id").value("2"))
        .andExpect(jsonPath("$[1].distribution").value("Web"));

    verify(service, times(1)).getAllDistributionChannels();
  }

  @Test
  void getDistributionChannelById_whenDistributionChannelIdInvalid_thenReturnNotFound()
      throws Exception {

    String invalidId = "some-invalid-id";

    when(service.getDistributionChannelById(invalidId))
        .thenThrow(CatalogItemNotFoundException.class);

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
        .andExpect(jsonPath("$.path").value("/distribution-channels/some-invalid-id"));

    verify(service, times(1)).getDistributionChannelById(invalidId);
  }

  @Test
  void getDistributionChannelById_whenDistributionChannelIdValid_thenReturnDistributionChannel()
      throws Exception {

    String id = "1";

    when(service.getDistributionChannelById(id)).thenReturn(new DistributionChannel(id, "Stores"));

    mockMvc
        .perform(get(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.distribution").value("Stores"));

    verify(service, times(1)).getDistributionChannelById(id);
  }

  @Test
  void updateDistributionChannel_whenDistributionChannelIdInvalid_thenReturnNotFound()
      throws Exception {
    String payload = loadPayload("distribution-channels/payload_success.json");
    String invalidId = "some-invalid-id";

    DistributionChannel existingDistributionChannel =
        fromJsonToObject(DistributionChannel.class, payload);

    when(service.updateDistributionChannel(invalidId, existingDistributionChannel))
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
        .andExpect(jsonPath("$.path").value("/distribution-channels/some-invalid-id"));

    verify(service, times(1)).updateDistributionChannel(invalidId, existingDistributionChannel);
  }

  @Test
  void
      updateDistributionChannel_whenDistributionChannelIdValid_thenReturnUpdatedDistributionChannel()
          throws Exception {
    String payload = loadPayload("distribution-channels/payload_success.json");
    String id = "1";

    DistributionChannel existingDistributionChannel =
        fromJsonToObject(DistributionChannel.class, payload);

    when(service.updateDistributionChannel(id, existingDistributionChannel))
        .thenReturn(new DistributionChannel(id, "Stores"));

    mockMvc
        .perform(put(PATH + "/{id}", id).contentType(APPLICATION_JSON).content(payload))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.distribution").value("Stores"));

    verify(service, times(1)).updateDistributionChannel(id, existingDistributionChannel);
  }

  @Test
  void deleteDistributionChannel_whenDistributionChannelIdValid_thenReturnNoContent()
      throws Exception {
    String id = "1";

    doNothing().when(service).deleteDistributionChannel(id);

    mockMvc
        .perform(delete(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(service, times(1)).deleteDistributionChannel(id);
  }
}
