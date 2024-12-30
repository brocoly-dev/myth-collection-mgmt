package com.mesofi.myth.collection.mgmt.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.repository.DistributorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DistributorServiceTest {
  @InjectMocks private DistributorService service;

  @Mock private DistributorRepository repository;

  @Test
  void createDistributor_whenDistributorPopulated_thenCreateDistributorAndReturnSaved() {

    // Arrange
    Distributor distributorToSave = new Distributor(null, "DAM");
    Distributor savedDistributor = new Distributor("1", "DAM");
    when(repository.save(distributorToSave)).thenReturn(savedDistributor);

    // Act
    Distributor result = service.createDistributor(distributorToSave);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("DAM", result.getName());

    // Verify
    verify(repository, times(1)).save(distributorToSave);
  }

  @Test
  void getAllDistributors_whenExistingDistributors_thenReturnAll() {

    // Arrange
    when(repository.findAll())
        .thenReturn(List.of(new Distributor("1", "DAM"), new Distributor("2", "DTM")));

    // Act
    List<Distributor> result = service.getAllDistributors();

    // Assert
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
    assertEquals("1", result.get(0).getId());
    assertEquals("DAM", result.get(0).getName());
    assertEquals("2", result.get(1).getId());
    assertEquals("DTM", result.get(1).getName());

    // Verify
    verify(repository).findAll();
  }

  @Test
  void getDistributorById_whenNoDistributor_thenThrowException() {

    // Arrange
    when(repository.findById("-")).thenReturn(Optional.empty());

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.getDistributorById("-"))
        .withMessage("Unable to find a valid distributor using id: -");
  }

  @Test
  void getDistributorById_whenDistributorFound_thenReturnValidDistributor() {

    // Arrange
    when(repository.findById("1")).thenReturn(Optional.of(new Distributor("1", "DTM")));

    // Act
    Distributor result = service.getDistributorById("1");

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("DTM", result.getName());

    // Verify
    verify(repository).findById("1");
  }

  @Test
  void updateDistributor_whenDistributorIdNotFound_thenThrowException() {

    // Arrange
    when(repository.existsById("-")).thenReturn(false);

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.updateDistributor("-", null))
        .withMessage("Unable to update existing distributor for a given id: -");
  }

  @Test
  void updateDistributor_whenDistributorFound_thenReturnUpdatedDistributor() {

    // Arrange
    Distributor existingDistributor = new Distributor("1", "D");
    when(repository.existsById("1")).thenReturn(true);
    when(repository.save(existingDistributor)).thenReturn(existingDistributor);

    // Act
    Distributor result = service.updateDistributor("1", existingDistributor);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("D", result.getName());

    // Verify
    verify(repository).existsById("1");
    verify(repository).save(existingDistributor);
  }

  @Test
  void deleteDistributor_whenExistingDistributor_thenDeleteIt() {

    // Arrange
    doNothing().when(repository).deleteById("1");

    // Act
    service.deleteDistributor("1");

    // Assert

    // Verify
    verify(repository).deleteById("1");
  }
}
