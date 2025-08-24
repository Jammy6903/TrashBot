package com.jami.Database;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.App;

public class GetorDefault {

  public static ObjectId ObjectId(Document doc, String key) {
    ObjectId value = doc.getObjectId(key);
    if (value != null) {
      return value;
    }
    return new ObjectId();
  }

  public static Document Document(Document doc, String key) {
    Document value = doc.get(key, Document.class);
    if (value != null) {
      return value;
    }
    return new Document();
  }

  public static String String(Document doc, String key, String defaultValue) {
    String value = doc.getString(key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  public static Integer Integer(Document doc, String key, Integer defaultValue) {
    Integer value = doc.getInteger(key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  public static Long Long(Document doc, String key, Long defaultValue) {
    Long value = doc.getLong(key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  public static Double Double(Document doc, String key, Double defaultValue) {
    Double value = doc.getDouble(key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  public static <T> List<T> List(Document doc, String key, Class<T> type, List<T> defaultValues) {
    List<T> values = doc.getList(key, type);
    if (values != null) {
      return values;
    }
    return defaultValues;
  }

  public static <T extends Enum<T>> T Enum(Document doc, String key, Class<T> type, T defaultValue) {
    String value = doc.getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Enum.valueOf(type, value);
    } catch (IllegalArgumentException e) {
      App.getLogger().error(e.getMessage());
      return defaultValue;
    }
  }

  public static <T extends Enum<T>> List<T> EnumList(Document doc, String key, Class<T> type, List<T> defaultValues) {
    List<String> values = doc.getList(key, String.class);
    if (values == null) {
      return defaultValues;
    }
    return values.stream()
        .map(v -> {
          try {
            return Enum.valueOf(type, v);
          } catch (Exception e) {
            App.getLogger().error(e.getMessage());
            return null;
          }
        })
        .collect(Collectors.toList());
  }
}
