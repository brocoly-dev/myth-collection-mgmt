package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.model.CatalogKeyDescription;
import com.mesofi.myth.collection.mgmt.model.LineUp;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LineUpService {

  public List<CatalogKeyDescription> getAllLineUps() {
    return Arrays.stream(LineUp.values())
        .map(lineUp -> new CatalogKeyDescription(lineUp.name(), lineUp.getDescription()))
        .toList();
  }

  public CatalogKeyDescription getLineUpById(String id) {
    try {
      LineUp lineUp = LineUp.valueOf(id);
      return new CatalogKeyDescription(lineUp.name(), lineUp.getDescription());
    } catch (IllegalArgumentException e) {
      throw notFound("Unable to find a valid lineUp using id: " + id).get();
    }
  }

  private Supplier<CatalogItemNotFoundException> notFound(final String message) {
    return () -> new CatalogItemNotFoundException(message);
  }
}
