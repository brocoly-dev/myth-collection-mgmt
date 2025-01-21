package com.mesofi.myth.collection.mgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MythCollectionServiceTest {
  @Mock private MythCollectionRepository repository;

  @InjectMocks private MythCollectionService service;

  @Test
  void createFigurine_whenFigurinePopulated_thenCreateFigurineAndReturnSaved() {

    // Arrange
    Figurine figurineToSave =
        new Figurine(
            null, "Seiya", null, null, "https://tamashiiweb.com/item/000", null, null, null);
    Figurine savedFigurine =
        new Figurine(
            "1", "Seiya", null, null, "https://tamashiiweb.com/item/000", null, null, null);
    when(repository.save(figurineToSave)).thenReturn(savedFigurine);

    // Act
    Figurine result = service.createFigurine(figurineToSave);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Seiya", result.getBaseName());
    assertEquals("https://tamashiiweb.com/item/000", result.getTamashiiUrl());
    verify(repository, times(1)).save(figurineToSave);
  }
}
