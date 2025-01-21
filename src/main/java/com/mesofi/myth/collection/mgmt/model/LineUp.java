package com.mesofi.myth.collection.mgmt.model;

public enum LineUp implements Describable {
  MYTH_CLOTH_EX("Myth Cloth EX"),
  MYTH_CLOTH("Myth Cloth"),
  APPENDIX("Appendix"),
  SC_LEGEND("Saint Cloth Legend"),
  FIGUARTS("Figuarts"),
  FIGUARTS_ZERO("Figuarts Zero Touche Metallique"),
  SC_CROWN("Saint Cloth Crown"),
  DDP("DD Panoramation");

  private final String description;

  LineUp(String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String getDescription() {
    return description;
  }
}
