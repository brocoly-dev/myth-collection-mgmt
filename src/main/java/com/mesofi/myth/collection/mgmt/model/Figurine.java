package com.mesofi.myth.collection.mgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "figurines")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Figurine {
  @Id private String id;

  @NotBlank
  @Size(min = 3, max = 20)
  private String baseName;

  @Valid private Distribution distributionJPY;
  @Valid private Distribution distributionMXN;

  @Size(max = 35)
  String tamashiiUrl;

  @Valid private DistributionChannel distributionChannel;
  private LineUp lineUp;
  private Series series;
  private Category category;
}
