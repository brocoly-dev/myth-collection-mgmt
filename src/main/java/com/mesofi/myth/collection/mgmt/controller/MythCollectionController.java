/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.service.MythCollectionService;
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
@RequestMapping("/figurines")
@CrossOrigin(origins = "http://localhost:3000")
public class MythCollectionController {

  private final MythCollectionService service;

  @PostMapping
  public ResponseEntity<Figurine> createFigurine(
      @RequestBody Figurine figurine, UriComponentsBuilder urBuilder) {

    Figurine newFigurine = service.createFigurine(figurine);

    // Build the URI for the newly created resource
    String location =
        urBuilder.path("/figurines/{id}").buildAndExpand(newFigurine.id()).toUriString();

    // Return 201 Created with Location header and the created figurine
    return ResponseEntity.created(URI.create(location)).body(newFigurine);
  }
}
