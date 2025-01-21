package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.model.Describable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service used to handle common catalogs. */
@Slf4j
@Service
public class CatalogService {

  /**
   * Gets all the elements contained in an enum type.
   *
   * @param <E> The type which must be an enum.
   * @param enumType The enum type used to extract the elements.
   * @return The key description for each enum element.
   */
  public <E extends Enum<E> & Describable> List<CatalogKeyDescription> getAllCatalogItems(
      Class<E> enumType) {
    return Arrays.stream(enumType.getEnumConstants())
        .map(e -> new CatalogKeyDescription(e.name(), e.getDescription()))
        .toList();
  }

  /**
   * Gets a catalog by a given identifier.
   *
   * @param <E> The type which must be an enum.
   * @param enumType The enum type used to extract the elements.
   * @param id The unique identifier for the enum element.
   * @return The catalog key description.
   */
  public <E extends Enum<E> & Describable> CatalogKeyDescription getCatalogById(
      Class<E> enumType, String id) {
    try {
      E e = Enum.valueOf(enumType, id);
      return new CatalogKeyDescription(e.name(), e.getDescription());
    } catch (IllegalArgumentException e) {
      throw notFound("Unable to find a valid catalog using id: " + id).get();
    }
  }

  private Supplier<CatalogItemNotFoundException> notFound(final String message) {
    return () -> new CatalogItemNotFoundException(message);
  }
}
