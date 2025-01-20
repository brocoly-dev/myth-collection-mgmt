package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.service.LineUpService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author armandorivasarzaluz
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(LineUpController.MAPPING)
@CrossOrigin(origins = "*")
public class LineUpController {

  public static final String MAPPING = "/lineups";

  private final LineUpService service;

  @GetMapping
  public List<CatalogKeyDescription> getAllLineUps() {
    return service.getAllLineUps();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CatalogKeyDescription> getLineUpById(@PathVariable String id) {
    return ResponseEntity.ok(service.getLineUpById(id));
  }
}
