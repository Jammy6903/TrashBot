package com.jami.database;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.database.guild.guildSettings.*;
import com.jami.database.user.userSettings;

public class getOrDefault {
  public static Long Long(Document doc, String key, Long defaultValue) {
    if (doc.getLong(key) != null) {
      return doc.getLong(key);
    }
    return defaultValue;
  }

  public static ObjectId ObjectId(Document doc, String key) {
    if (doc.getObjectId(key) != null) {
      return doc.getObjectId(key);
    }
    return new ObjectId();
  }

  public static guildSettings guildSettings(Document doc, String key) {
    if (doc != null) {
      return new guildSettings(doc.get(key, Document.class));
    }
    return new guildSettings(null);
  }

  public static userSettings userSettings(Document doc, String key) {
    if (doc != null) {
      return new userSettings(doc.get(key, Document.class));
    }
    return new userSettings(null);
  }
}
