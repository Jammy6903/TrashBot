package com.jami.Database.Enumerators;

import java.util.List;
import java.util.stream.Collectors;

public class EnumToString {
  public static <T extends Enum<T>> String get(T enumValue) {
    return enumValue.toString();
  }

  public static <T extends Enum<T>> List<String> getFromList(List<T> enumValues) {
    return enumValues.stream()
        .map(v -> {
          return v.name();
        })
        .collect(Collectors.toList());
  }
}
