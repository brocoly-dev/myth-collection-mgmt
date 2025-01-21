package com.mesofi.myth.collection.mgmt.controller;

import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author armandorivasarzaluz
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(CategoryController.MAPPING)
public class CategoryController extends CatalogControllable<Category> {

  public static final String MAPPING = "/categories";

  protected CategoryController(CatalogService catalogService) {
    super(catalogService);
  }

  /** {@inheritDoc} */
  @Override
  protected Class<Category> getType() {
    return Category.class;
  }
}
