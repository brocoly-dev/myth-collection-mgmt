package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author armandorivasarzaluz
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(SeriesController.MAPPING)
public class SeriesController extends CatalogControllable<Series> {

  public static final String MAPPING = "/series";

  protected SeriesController(CatalogService catalogService) {
    super(catalogService);
  }

  /** {@inheritDoc} */
  @Override
  protected Class<Series> getType() {
    return Series.class;
  }
}
