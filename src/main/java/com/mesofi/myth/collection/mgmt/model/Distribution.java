package com.mesofi.myth.collection.mgmt.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Distribution {
  private BigDecimal basePrice;
  private BigDecimal finalPrice;
  private LocalDate firstAnnouncementDate;
  private LocalDate preOrderDate;
  private LocalDate releaseDate;
}
