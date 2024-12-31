package com.mesofi.myth.collection.mgmt.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mesofi.myth.collection.mgmt.model.Distributor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("itest")
public class DistributorRepositoryTest {

  @Autowired private DistributorRepository repository;

  @Test
  void save_whenDistributorPopulated_thenCreateDistributorAndReturnSaved() {
    Distributor distributorToSave = new Distributor();
    distributorToSave.setName("DAM");
    Distributor savedDistributor = repository.save(distributorToSave);

    assertTrue(repository.findById(savedDistributor.getId()).isPresent());
  }
}
