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

  @Valid private Distribution distributionJPY;
  @Valid private Distribution distributionMXN;

  @Size(max = 35)
  private String tamashiiUrl;

  @Valid private DistributionChannel distributionChannel;

  private String remarks;
}
