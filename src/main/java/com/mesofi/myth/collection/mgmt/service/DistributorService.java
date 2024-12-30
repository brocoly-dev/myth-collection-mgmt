package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.repository.DistributorRepository;
import java.util.List;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DistributorService {

  private final DistributorRepository repository;

  public Distributor createDistributor(Distributor distributor) {
    log.info("A new distributor is about to be created ...");

    return repository.save(distributor);
  }

  public List<Distributor> getAllDistributors() {
    return repository.findAll();
  }

  public Distributor getDistributorById(String id) {
    return repository
        .findById(id)
        .orElseThrow(notFound("Unable to find a valid distributor using id: " + id));
  }

  public Distributor updateDistributor(String id, Distributor distributor) {
    if (repository.existsById(id)) {
      distributor.setId(id);
      return repository.save(distributor);
    }
    throw notFound("Unable to update existing distributor for a given id: " + id).get();
  }

  public void deleteDistributor(String id) {
    repository.deleteById(id);
  }

  private Supplier<IllegalArgumentException> notFound(final String message) {
    return () -> new IllegalArgumentException(message);
  }
}
