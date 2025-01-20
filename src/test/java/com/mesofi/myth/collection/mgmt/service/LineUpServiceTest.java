package com.mesofi.myth.collection.mgmt.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineUpServiceTest {
  @InjectMocks private LineUpService service;

  @Test
  void getAllLineUps_whenExistingLineUps_thenReturnAll() {

    // Act
    List<CatalogKeyDescription> result = service.getAllLineUps();

    // Assert
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(8, result.size());
    assertEquals("MYTH_CLOTH_EX", result.get(0).getKey());
    assertEquals("Myth Cloth EX", result.get(0).getDescription());
    assertEquals("MYTH_CLOTH", result.get(1).getKey());
    assertEquals("Myth Cloth", result.get(1).getDescription());
    assertEquals("APPENDIX", result.get(2).getKey());
    assertEquals("Appendix", result.get(2).getDescription());
    assertEquals("SC_LEGEND", result.get(3).getKey());
    assertEquals("Saint Cloth Legend", result.get(3).getDescription());
    assertEquals("FIGUARTS", result.get(4).getKey());
    assertEquals("Figuarts", result.get(4).getDescription());
    assertEquals("FIGUARTS_ZERO", result.get(5).getKey());
    assertEquals("Figuarts Zero Touche Metallique", result.get(5).getDescription());
    assertEquals("SC_CROWN", result.get(6).getKey());
    assertEquals("Saint Cloth Crown", result.get(6).getDescription());
    assertEquals("DDP", result.get(7).getKey());
    assertEquals("DD Panoramation", result.get(7).getDescription());
  }

  @Test
  void getLineUpById_whenNoLineUp_thenThrowException() {

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.getLineUpById("-"))
        .withMessage("Unable to find a valid lineUp using id: -");
  }

  @Test
  void getLineUpById_whenlineUpFound_thenReturnValidLineUp() {

    // Act
    CatalogKeyDescription result = service.getLineUpById("MYTH_CLOTH");

    // Assert
    assertNotNull(result);
    assertEquals("MYTH_CLOTH", result.getKey());
    assertEquals("Myth Cloth", result.getDescription());
  }
}
