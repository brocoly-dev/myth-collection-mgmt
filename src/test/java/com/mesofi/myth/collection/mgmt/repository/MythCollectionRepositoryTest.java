package com.mesofi.myth.collection.mgmt.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("itest")
public class MythCollectionRepositoryTest {

  @Autowired private MythCollectionRepository repository;

  @Test
  void save_whenFigurinePopulated_thenCreateFigurineAndReturnSaved() {
    Figurine figurineToSave =
        new Figurine(
            null, "Seiya", null, null, "https://tamashiiweb.com/item/000", null, null, null);
    Figurine savedFigurine = repository.save(figurineToSave);

    assertTrue(repository.findById(savedFigurine.getId()).isPresent());
  }
}
