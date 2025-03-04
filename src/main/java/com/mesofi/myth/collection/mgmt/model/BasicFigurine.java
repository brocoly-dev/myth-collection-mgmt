package com.mesofi.myth.collection.mgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * @see Figurine
 */
public class BasicFigurine {
  @EqualsAndHashCode.Exclude @Id private String id; // generated by the DB

  private String displayableName; // This field is calculated ...

  private LineUp lineUp;
  private Category category;
  private Status status; // This field is calculated ...
}
