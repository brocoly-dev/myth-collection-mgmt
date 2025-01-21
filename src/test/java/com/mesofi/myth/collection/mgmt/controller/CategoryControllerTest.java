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
import com.mesofi.myth.collection.mgmt.model.Category;
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
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

  @Autowired private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests

  @MockitoBean private CatalogService service; // Mock the service layer

  private final String PATH = "/categories";

  @Test
  void getAllCatalogItems_whenCategoryPopulated_thenReturnAllCategories() throws Exception {

    when(service.getAllCatalogItems(Category.class))
        .thenReturn(
            List.of(
                new CatalogKeyDescription("STEEL", "Steel"),
                new CatalogKeyDescription("GOD", "God")));

    mockMvc
        .perform(get(PATH).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].key").value("STEEL"))
        .andExpect(jsonPath("$[0].description").value("Steel"))
        .andExpect(jsonPath("$[1].key").value("GOD"))
        .andExpect(jsonPath("$[1].description").value("God"));

    verify(service, times(1)).getAllCatalogItems(Category.class);
  }

  @Test
  void getAllCatalogItems_whenCategoryIdInvalid_thenReturnNotFound() throws Exception {

    String invalidId = "some-invalid-id";

    when(service.getCatalogById(Category.class, invalidId))
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
        .andExpect(jsonPath("$.path").value("/categories/some-invalid-id"));

    verify(service, times(1)).getCatalogById(Category.class, invalidId);
  }

  @Test
  void getAllCatalogItems_whenCategoryIdValid_thenReturnCategories() throws Exception {

    String id = "SAINT_SEIYA";

    when(service.getCatalogById(Category.class, id))
        .thenReturn(new CatalogKeyDescription("GOLD", "Golden Saint"));

    mockMvc
        .perform(get(PATH + "/{id}", id).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.key").value("GOLD"))
        .andExpect(jsonPath("$.description").value("Golden Saint"));

    verify(service, times(1)).getCatalogById(Category.class, id);
  }
}
