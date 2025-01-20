package com.mesofi.myth.collection.mgmt.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.service.LineUpService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LineUpController.class)
public class LineUpControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private LineUpService service; // Mock the service layer

  private final String PATH = "/lineups";

  @Test
  void getAllLineUps_whenLineUpsPopulated_thenReturnAllLineUps() throws Exception {

    when(service.getAllLineUps())
        .thenReturn(
            List.of(
                new CatalogKeyDescription("MYTH_CLOTH_EX", "Myth Cloth EX"),
                new CatalogKeyDescription("MYTH_CLOTH", "Myth Cloth")));

    mockMvc
        .perform(get(PATH).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].key").value("MYTH_CLOTH_EX"))
        .andExpect(jsonPath("$[0].description").value("Myth Cloth EX"))
        .andExpect(jsonPath("$[1].key").value("MYTH_CLOTH"))
        .andExpect(jsonPath("$[1].description").value("Myth Cloth"));

    verify(service, times(1)).getAllLineUps();
  }

  @Test
  void getLineUpById_whenLineUpIdInvalid_thenReturnNotFound() throws Exception {

    String invalidId = "some-invalid-id";

    when(service.getLineUpById(invalidId)).thenThrow(CatalogItemNotFoundException.class);

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
        .andExpect(jsonPath("$.path").value("/lineups/some-invalid-id"));

    verify(service, times(1)).getLineUpById(invalidId);
  }

  @Test
  void getLineUpById_whenLineUpIdValid_thenReturnLineUp() throws Exception {

    String id = "DDP";

    when(service.getLineUpById(id)).thenReturn(new CatalogKeyDescription("DDP", "DD Panoramation"));

    mockMvc
        .perform(get(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.key").value("DDP"))
        .andExpect(jsonPath("$.description").value("DD Panoramation"));

    verify(service, times(1)).getLineUpById(id);
  }
}
