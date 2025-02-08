package com.mesofi.myth.collection.mgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Distribution {
  @Valid private Distributor distributor;
  @NotNull private BigDecimal basePrice;
  private BigDecimal finalPrice; // This field is calculated ...
  private LocalDate firstAnnouncementDate;
  @NotNull private LocalDate preOrderDate;
  @NotNull private LocalDate releaseDate;
  @NotNull private Boolean releaseDateConfirmed;
}
