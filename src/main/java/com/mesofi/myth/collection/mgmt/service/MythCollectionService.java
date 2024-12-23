package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MythCollectionService {

  private final MythCollectionRepository repository;

  public Figurine createFigurine(Figurine figurine) {
    log.info("A new figure is about to be created ...");

    Figurine created = repository.save(figurine);
    log.info("A new figure has been created with id: {}", created.id());
    return created;
  }
}
