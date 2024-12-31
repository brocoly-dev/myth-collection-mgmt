package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.service.MythCollectionService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(MythCollectionController.MAPPING)
@CrossOrigin(origins = "*")
public class MythCollectionController {

  public static final String MAPPING = "/figurines";

  private final MythCollectionService service;

  @PostMapping
  public ResponseEntity<Figurine> createFigurine(
      @RequestBody @Valid Figurine figurine, UriComponentsBuilder urBuilder) {

    Figurine newFigurine = service.createFigurine(figurine);

    // Build the URI for the newly created resource
    String pathLocation = MAPPING + "/{id}";
    String location =
        urBuilder.path(pathLocation).buildAndExpand(newFigurine.getId()).toUriString();

    // Return 201 Created with Location header and the created figurine
    return ResponseEntity.created(URI.create(location)).body(newFigurine);
  }
}
