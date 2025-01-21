package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.model.Describable;
import com.mesofi.myth.collection.mgmt.service.CatalogService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This controller must be used for the generic catalogs.
 *
 * @param <E> The type to be used.
 */
public abstract class CatalogControllable<E extends Enum<E> & Describable> {
  private final CatalogService catalogService;

  protected CatalogControllable(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  /**
   * Return all existing catalogs.
   *
   * @return Gets all existing items.
   */
  @GetMapping
  public List<CatalogKeyDescription> getAllCatalogItems() {
    return catalogService.getAllCatalogItems(getType());
  }

  /**
   * Gets a specific catalog by identifier.
   *
   * @param id The unique identifier.
   * @return The catalog by id.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CatalogKeyDescription> getCatalogById(@PathVariable String id) {
    return ResponseEntity.ok(catalogService.getCatalogById(getType(), id));
  }

  /**
   * The enum type for the catalogs.
   *
   * @return The type.
   */
  protected abstract Class<E> getType();
}
