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
import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.service.CatalogService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SeriesController.class)
public class SeriesControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private CatalogService service; // Mock the service layer

  private final String PATH = "/series";

  @Test
  void getAllCatalogItems_whenSeriesPopulated_thenReturnAllSeries() throws Exception {

    when(service.getAllCatalogItems(Series.class))
        .thenReturn(
            List.of(
                new CatalogKeyDescription("SAINT_SEIYA", "Saint Seiya"),
                new CatalogKeyDescription("SOG", "Soul of Gold")));

    mockMvc
        .perform(get(PATH).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].key").value("SAINT_SEIYA"))
        .andExpect(jsonPath("$[0].description").value("Saint Seiya"))
        .andExpect(jsonPath("$[1].key").value("SOG"))
        .andExpect(jsonPath("$[1].description").value("Soul of Gold"));

    verify(service, times(1)).getAllCatalogItems(Series.class);
  }

  @Test
  void getAllCatalogItems_whenSeriesIdInvalid_thenReturnNotFound() throws Exception {

    String invalidId = "some-invalid-id";

    when(service.getCatalogById(Series.class, invalidId))
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
        .andExpect(jsonPath("$.path").value("/series/some-invalid-id"));

    verify(service, times(1)).getCatalogById(Series.class, invalidId);
  }

  @Test
  void getAllCatalogItems_whenSeriesIdValid_thenReturnSeries() throws Exception {

    String id = "SAINT_SEIYA";

    when(service.getCatalogById(Series.class, id))
        .thenReturn(new CatalogKeyDescription("SAINT_SEIYA", "Saint Seiya"));

    mockMvc
        .perform(get(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.key").value("SAINT_SEIYA"))
        .andExpect(jsonPath("$.description").value("Saint Seiya"));

    verify(service, times(1)).getCatalogById(Series.class, id);
  }
}
