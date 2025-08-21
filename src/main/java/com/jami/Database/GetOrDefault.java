package com.jami.Database;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.Database.Guild.guildSettings.*;
import com.jami.Database.Guild.guildSettings.WelcomeMessage.WelcomeMessageType;
import com.jami.Database.User.UserSettings;

public class GetOrDefault {
  public static String String(Document doc, String key, String defaultValue) {
    if (doc.getString(key) != null) {
      return doc.getString(key);
    }
    return defaultValue;
  }

  public static Long Long(Document doc, String key, Long defaultValue) {
    if (doc.getLong(key) != null) {
      return doc.getLong(key);
    }
    return defaultValue;
  }

  public static Double Double(Document doc, String key, Double defaultValue) {
    if (doc.getDouble(key) != null) {
      return doc.getDouble(key);
    }
    return defaultValue;
  }

  public static WelcomeMessageType WelcomeMessageType(Document doc, String key, WelcomeMessageType defaultType) {
    if (doc.get(key, WelcomeMessageType.class) != null) {
      return doc.get(key, WelcomeMessageType.class);
    }
    return defaultType;
  }

  public static ObjectId ObjectId(Document doc, String key) {
    if (doc.getObjectId(key) != null) {
      return doc.getObjectId(key);
    }
    return new ObjectId();
  }

  public static GuildSettings guildSettings(Document doc, String key) {
    if (doc != null) {
      return new GuildSettings(doc.get(key, Document.class));
    }
    return new GuildSettings(null);
  }

  public static UserSettings userSettings(Document doc, String key) {
    if (doc != null) {
      return new UserSettings(doc.get(key, Document.class));
    }
    return new UserSettings(null);
  }
}
