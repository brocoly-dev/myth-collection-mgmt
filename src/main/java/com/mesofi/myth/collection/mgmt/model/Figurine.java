package com.mesofi.myth.collection.mgmt.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "figurines")
public record Figurine(
    @Id String id,
    @NotBlank @Size(min = 3, max = 20) String baseName,
    @Size(max = 35) String tamashiiUrl) {}
