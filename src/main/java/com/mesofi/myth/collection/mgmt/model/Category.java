package com.mesofi.myth.collection.mgmt.model;

public enum Category implements Describable {
  GOLD("Golden Saint");

  private final String description;

  Category(String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String getDescription() {
    return description;
  }
}
