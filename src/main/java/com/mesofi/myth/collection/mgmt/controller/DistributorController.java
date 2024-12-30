package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.service.DistributorService;
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
@RequestMapping(DistributorController.MAPPING)
@CrossOrigin(origins = "http://localhost:3000")
public class DistributorController {

  public static final String MAPPING = "/distributors";

  private final DistributorService service;

  @PostMapping
  public ResponseEntity<Distributor> createDistributor(
      @RequestBody @Valid Distributor distributor, UriComponentsBuilder urBuilder) {

    Distributor newDistributor = service.createDistributor(distributor);

    // Build the URI for the newly created resource
    String pathLocation = MAPPING + "/{id}";
    String location =
        urBuilder.path(pathLocation).buildAndExpand(newDistributor.getId()).toUriString();

    // Return 201 Created with Location header and the created distributor
    return ResponseEntity.created(URI.create(location)).body(newDistributor);
  }

  @GetMapping
  public List<Distributor> getAllDistributors() {
    return service.getAllDistributors();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Distributor> getDistributorById(@PathVariable String id) {
    return ResponseEntity.ok(service.getDistributorById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Distributor> updateDistributor(
      @PathVariable String id, @RequestBody @Valid Distributor distributor) {
    return ResponseEntity.ok(service.updateDistributor(id, distributor));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Distributor> deleteDistributor(@PathVariable String id) {
    service.deleteDistributor(id);
    return ResponseEntity.noContent().build();
  }
}
