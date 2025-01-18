package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.repository.DistributionChannelRepository;
import java.util.List;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DistributionChannelService {

  private final DistributionChannelRepository repository;

  public DistributionChannel createDistributionChannel(DistributionChannel distributionChannel) {
    log.info("A new distribution channel is about to be created ...");

    return repository.save(distributionChannel);
  }

  public List<DistributionChannel> getAllDistributionChannels() {
    return repository.findAll();
  }

  public DistributionChannel getDistributionChannelById(String id) {
    return repository
        .findById(id)
        .orElseThrow(notFound("Unable to find a valid distribution channel using id: " + id));
  }

  public DistributionChannel updateDistributionChannel(
      String id, DistributionChannel distributionChannel) {
    if (repository.existsById(id)) {
      distributionChannel.setId(id);
      return repository.save(distributionChannel);
    }
    throw notFound("Unable to update existing distribution channel for a given id: " + id).get();
  }

  public void deleteDistributionChannel(String id) {
    repository.deleteById(id);
  }

  private Supplier<CatalogItemNotFoundException> notFound(final String message) {
    return () -> new CatalogItemNotFoundException(message);
  }
}
