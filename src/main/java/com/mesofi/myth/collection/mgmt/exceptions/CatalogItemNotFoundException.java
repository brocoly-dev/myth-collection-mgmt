package com.mesofi.myth.collection.mgmt.exceptions;

public class CatalogItemNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1633194847674823762L;

  public CatalogItemNotFoundException(String msg) {
    super(msg);
  }
}
