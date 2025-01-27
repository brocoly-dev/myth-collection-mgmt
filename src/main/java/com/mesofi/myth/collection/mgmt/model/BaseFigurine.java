package com.mesofi.myth.collection.mgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseFigurine {

  @EqualsAndHashCode.Exclude @Valid private Distribution distributionJPY;
  @EqualsAndHashCode.Exclude @Valid private Distribution distributionMXN;

  @Size(max = 35)
  @EqualsAndHashCode.Exclude
  private String tamashiiUrl;

  @EqualsAndHashCode.Exclude @Valid private DistributionChannel distributionChannel;

  @EqualsAndHashCode.Exclude private String remarks;
}
