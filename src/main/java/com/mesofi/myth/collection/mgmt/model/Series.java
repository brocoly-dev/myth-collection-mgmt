package com.mesofi.myth.collection.mgmt.model;

public enum Series implements Describable {
  SAINT_SEIYA("Saint Seiya"),
  SAINTIA_SHO("Saintia Sho"),
  SOG("Soul of Gold"),
  SS_LEGEND_OF_SANTUARY("Saint Seiya Legend Of Sanctuary"),
  SS_OMEGA("Saint Seiya Omega"),
  LOST_CANVAS("The Lost Canvas"),
  SS_THE_BEGINNING("Saint Seiya The Beginning");

  private final String description;

  Series(String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String getDescription() {
    return description;
  }
}
