package com.mesofi.myth.collection.mgmt.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("itest")
public class DistributionChannelRepositoryTest {

  @Autowired private DistributionChannelRepository repository;

  @Test
  void save_whenDistributionChannelPopulated_thenCreateDistributionChannelAndReturnSaved() {
    DistributionChannel distributionChannelToSave = new DistributionChannel();
    distributionChannelToSave.setDistribution("Stores");
    DistributionChannel savedDistributionChannel = repository.save(distributionChannelToSave);

    assertTrue(repository.findById(savedDistributionChannel.getId()).isPresent());
  }
}
