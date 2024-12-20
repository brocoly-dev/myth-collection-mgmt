package com.mesofi.myth.collection.mgmt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class MythCollectionRepositoryTest {

  @Autowired private MythCollectionRepository repository;

  @Test
  void save_whenFigurinePopulated_thenCreateFigurineAndReturnSaved() {
    Figurine figurineToSave = new Figurine(null, "Seiya");
    Figurine savedFigurine = repository.save(figurineToSave);

    assertThat(repository.findById(savedFigurine.id())).isPresent();
  }
}
