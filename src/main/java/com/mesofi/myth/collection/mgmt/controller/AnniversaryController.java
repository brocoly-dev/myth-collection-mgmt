package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author armandorivasarzaluz
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(AnniversaryController.MAPPING)
public class AnniversaryController extends CatalogControllable<Anniversary> {

  public static final String MAPPING = "/anniversaries";

  protected AnniversaryController(CatalogService catalogService) {
    super(catalogService);
  }

  /** {@inheritDoc} */
  @Override
  protected Class<Anniversary> getType() {
    return Anniversary.class;
  }
}
