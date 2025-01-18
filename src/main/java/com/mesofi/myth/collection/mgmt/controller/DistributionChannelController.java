package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.service.DistributionChannelService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author armandorivasarzaluz
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(DistributionChannelController.MAPPING)
@CrossOrigin(origins = "*")
public class DistributionChannelController {

  public static final String MAPPING = "/distribution-channels";

  private final DistributionChannelService service;

  @PostMapping
  public ResponseEntity<DistributionChannel> createDistributionChannel(
      @RequestBody @Valid DistributionChannel distributionChannel, UriComponentsBuilder urBuilder) {

    DistributionChannel newDistributionChannel =
        service.createDistributionChannel(distributionChannel);

    // Build the URI for the newly created resource
    String pathLocation = MAPPING + "/{id}";
    String location =
        urBuilder.path(pathLocation).buildAndExpand(newDistributionChannel.getId()).toUriString();

    // Return 201 Created with Location header and the created DistributionChannel
    return ResponseEntity.created(URI.create(location)).body(newDistributionChannel);
  }

  @GetMapping
  public List<DistributionChannel> getAllDistributionChannels() {
    return service.getAllDistributionChannels();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DistributionChannel> getDistributionChannelById(@PathVariable String id) {
    return ResponseEntity.ok(service.getDistributionChannelById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DistributionChannel> updateDistributionChannel(
      @PathVariable String id, @RequestBody @Valid DistributionChannel distributionChannel) {
    return ResponseEntity.ok(service.updateDistributionChannel(id, distributionChannel));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DistributionChannel> deleteDistributionChannel(@PathVariable String id) {
    service.deleteDistributionChannel(id);
    return ResponseEntity.noContent().build();
  }
}
