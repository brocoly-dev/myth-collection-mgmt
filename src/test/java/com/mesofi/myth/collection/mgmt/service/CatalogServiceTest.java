package com.mesofi.myth.collection.mgmt.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.model.Series;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {
  @InjectMocks private CatalogService service;

  @Test
  void getAllCatalogItems_whenExistingItems_thenReturnAll() {

    // Act
    List<CatalogKeyDescription> result = service.getAllCatalogItems(Series.class);

    // Assert
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(7, result.size());
    assertEquals("SAINT_SEIYA", result.get(0).getKey());
    assertEquals("Saint Seiya", result.get(0).getDescription());
    assertEquals("SAINTIA_SHO", result.get(1).getKey());
    assertEquals("Saintia Sho", result.get(1).getDescription());
    assertEquals("SOG", result.get(2).getKey());
    assertEquals("Soul of Gold", result.get(2).getDescription());
    assertEquals("SS_LEGEND_OF_SANTUARY", result.get(3).getKey());
    assertEquals("Saint Seiya Legend Of Sanctuary", result.get(3).getDescription());
    assertEquals("SS_OMEGA", result.get(4).getKey());
    assertEquals("Saint Seiya Omega", result.get(4).getDescription());
    assertEquals("LOST_CANVAS", result.get(5).getKey());
    assertEquals("The Lost Canvas", result.get(5).getDescription());
    assertEquals("SS_THE_BEGINNING", result.get(6).getKey());
    assertEquals("Saint Seiya The Beginning", result.get(6).getDescription());
  }

  @Test
  void getCatalogById_whenNoCatalog_thenThrowException() {

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.getCatalogById(Series.class, "-"))
        .withMessage("Unable to find a valid catalog using id: -");
  }

  @Test
  void getCatalogById_whenCatalogFound_thenReturnValidCatalog() {

    // Act
    CatalogKeyDescription result = service.getCatalogById(Series.class, "SAINT_SEIYA");

    // Assert
    assertNotNull(result);
    assertEquals("SAINT_SEIYA", result.getKey());
    assertEquals("Saint Seiya", result.getDescription());
  }
}
