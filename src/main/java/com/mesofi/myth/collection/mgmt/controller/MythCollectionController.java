package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.BasicFigurine;
import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.service.MythCollectionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

  @PostMapping("/upload")
  public List<Figurine> uploadFigurines(@RequestParam("file") MultipartFile file) {

    List<Figurine> figurineList = service.createFigurines(file);
    log.info("{} figurines created ...", figurineList.size());

    return figurineList;
  }

  /**
   * Creates a new figurine.
   *
   * @param figurine The figurine to be created.
   * @param uriBuilder The Uri builder.
   * @return The figurine created.
   */
  @PostMapping
  public ResponseEntity<Figurine> createFigurine(
      @RequestBody @Valid Figurine figurine, UriComponentsBuilder uriBuilder) {

    Figurine newFigurine = service.createFigurine(figurine);

    // Build the URI for the newly created resource
    String pathLocation = MAPPING + "/{id}";
    String location =
        uriBuilder.path(pathLocation).buildAndExpand(newFigurine.getId()).toUriString();

    // Return 201 Created with Location header and the created figurine
    return ResponseEntity.created(URI.create(location)).body(newFigurine);
  }

  /**
   * Update an existing figurine.
   *
   * @param id Unique identifier for the figurine.
   * @param figurine The figurine with updated information.
   * @return The resulting figurine.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Figurine> updateFigurine(
      @PathVariable String id, @RequestBody @Valid Figurine figurine) {

    Figurine updatedFigurine = service.updateFigurine(id, figurine);
    return ResponseEntity.ok(updatedFigurine);
  }

  @GetMapping("/basics")
  public List<BasicFigurine> getAllBasicFigurines(
      @RequestParam(required = false) boolean excludeRestocks) {
    return service.getAllBasicFigurines(excludeRestocks);
  }

  /**
   * Retrieves all the existing figurines.
   *
   * @param excludeRestocks By default, the restocks are included in the list.
   * @return A list of figurines.
   */
  @GetMapping
  public List<Figurine> getAllFigurines(@RequestParam(required = false) boolean excludeRestocks) {
    return service.getAllFigurines(excludeRestocks);
  }

  /**
   * Retrieves a single figure by its id.
   *
   * @param id The unique identifier.
   * @return The figurine.
   */
  @GetMapping("/{id}")
  public Figurine getFigurine(@PathVariable String id) {
    return service.getFigurine(id);
  }
}
