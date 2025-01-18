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
import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.repository.DistributionChannelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DistributionChannelServiceTest {
  @InjectMocks private DistributionChannelService service;

  @Mock private DistributionChannelRepository repository;

  @Test
  void
      createDistributionChannel_whenDistributionChannelPopulated_thenCreateDistributionChannelAndReturnSaved() {

    // Arrange
    DistributionChannel distributionChannelToSave = new DistributionChannel(null, "Stores");
    DistributionChannel savedDistributionChannel = new DistributionChannel("1", "Stores");
    when(repository.save(distributionChannelToSave)).thenReturn(savedDistributionChannel);

    // Act
    DistributionChannel result = service.createDistributionChannel(distributionChannelToSave);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Stores", result.getDistribution());

    // Verify
    verify(repository, times(1)).save(distributionChannelToSave);
  }

  @Test
  void getAllDistributionChannels_whenExistingDistributionChannels_thenReturnAll() {

    // Arrange
    when(repository.findAll())
        .thenReturn(
            List.of(new DistributionChannel("1", "Stores"), new DistributionChannel("2", "Shop")));

    // Act
    List<DistributionChannel> result = service.getAllDistributionChannels();

    // Assert
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
    assertEquals("1", result.get(0).getId());
    assertEquals("Stores", result.get(0).getDistribution());
    assertEquals("2", result.get(1).getId());
    assertEquals("Shop", result.get(1).getDistribution());

    // Verify
    verify(repository).findAll();
  }

  @Test
  void getDistributionChannelById_whenNoDistributionChannel_thenThrowException() {

    // Arrange
    when(repository.findById("-")).thenReturn(Optional.empty());

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.getDistributionChannelById("-"))
        .withMessage("Unable to find a valid distribution channel using id: -");
  }

  @Test
  void
      getDistributionChannelById_whenDistributionChannelFound_thenReturnValidDistributionChannel() {

    // Arrange
    when(repository.findById("1")).thenReturn(Optional.of(new DistributionChannel("1", "Stores")));

    // Act
    DistributionChannel result = service.getDistributionChannelById("1");

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Stores", result.getDistribution());

    // Verify
    verify(repository).findById("1");
  }

  @Test
  void updateDistributionChannel_whenDistributionChannelIdNotFound_thenThrowException() {

    // Arrange
    when(repository.existsById("-")).thenReturn(false);

    // Assert
    assertThatExceptionOfType(CatalogItemNotFoundException.class)
        .isThrownBy(() -> service.updateDistributionChannel("-", null))
        .withMessage("Unable to update existing distribution channel for a given id: -");
  }

  @Test
  void
      updateDistributionChannel_whenDistributionChannelFound_thenReturnUpdatedDistributionChannel() {

    // Arrange
    DistributionChannel distributionChannelToUpdate = new DistributionChannel("1", "Stores");
    DistributionChannel existingDistributionChannel = new DistributionChannel("1", "Stores");
    when(repository.existsById("1")).thenReturn(true);
    when(repository.save(distributionChannelToUpdate)).thenReturn(existingDistributionChannel);

    // Act
    DistributionChannel result =
        service.updateDistributionChannel("1", existingDistributionChannel);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Stores", result.getDistribution());
    assertEquals("1", existingDistributionChannel.getId());
    assertEquals("1", distributionChannelToUpdate.getId());

    // Verify
    verify(repository).existsById("1");
    verify(repository).save(existingDistributionChannel);
  }

  @Test
  void deleteDistributionChannel_whenExistingDistributionChannel_thenDeleteIt() {

    // Arrange
    doNothing().when(repository).deleteById("1");

    // Act
    service.deleteDistributionChannel("1");

    // Assert

    // Verify
    verify(repository).deleteById("1");
  }
}
