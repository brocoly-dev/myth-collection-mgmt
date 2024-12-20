package com.mesofi.myth.collection.mgmt.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Figurine(String id, @NotBlank @Size(min = 3, max = 20) String baseName) {}
