package com.mesofi.myth.collection.mgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDetails(String error, String[] messages, String detailMessage, String path) {}
