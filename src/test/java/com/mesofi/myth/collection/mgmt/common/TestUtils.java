package com.mesofi.myth.collection.mgmt.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static String loadPayload(String fileName) {
    return loadRegularFile("payloads", fileName);
  }

  public static String loadFigurines(String fileName) {
    return loadRegularFile("figurines", fileName);
  }

  private static String loadRegularFile(String folderName, String fileName) {
    try {
      return new ClassPathResource(folderName + "/" + fileName)
          .getContentAsString(Charset.defaultCharset());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to load file", e);
    }
  }

  public static <T> T fromJsonToObject(Class<T> clazz, String jsonString) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      return mapper.readValue(jsonString, clazz);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
