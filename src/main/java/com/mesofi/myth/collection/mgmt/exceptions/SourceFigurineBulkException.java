package com.mesofi.myth.collection.mgmt.exceptions;

import java.io.Serial;

public class SourceFigurineBulkException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1633194847674823762L;

  public SourceFigurineBulkException(String msg) {
    super(msg);
  }
}
