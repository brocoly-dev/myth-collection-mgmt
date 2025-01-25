package com.mesofi.myth.collection.mgmt.model;

public enum Category implements Describable {
  V1("Bronze Saint V1"),
  V2("Bronze Saint V2"),
  V3("Bronze Saint V3"),
  V4("Bronze Saint V4"),
  V5("Bronze Saint V5"),
  SECONDARY("Bronze Secondary"),
  BLACK("Black Saint"),
  STEEL("Steel"),
  SILVER("Silver Saint"),
  GOLD("Golden Saint"),
  ROBE("God Robe"),
  SCALE("Poseidon Scale"),
  SURPLICE("Surplice Saint"),
  SPECTER("Specter"),
  JUDGE("Judge"),
  GOD("God"),
  INHERITOR("Inheritor");
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
