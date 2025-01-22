package com.mesofi.myth.collection.mgmt.model;

public enum Anniversary implements Describable {
  A_10("10"),
  A_15("15"),
  A_20("20"),
  A_40("40"),
  A_50("50");
  private final String description;

  Anniversary(String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String getDescription() {
    return description;
  }
}
