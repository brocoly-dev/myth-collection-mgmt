package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MythCollectionService {

  public Figurine createFigurine(Figurine figurine) {
    log.info("A new figure is about to be created ...");
    return new Figurine("1", "d");
  }
}
