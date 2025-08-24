package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Config.ConfigRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class ConfigRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> configs = database.getCollection("CONFIGS");

  public static ConfigRecord getById(String id) {
    Document doc = configs.find(eq("_id", new ObjectId(id))).first();
    if (doc == null) {
      return null;
    }
    return new ConfigRecord(doc);
  }

  public static ConfigRecord getByName(String name) {
    Document doc = configs.find(eq("configName", name)).first();
    if (doc == null) {
      return new ConfigRecord(name);
    }
    return new ConfigRecord(doc);
  }

  public static void save(ConfigRecord record) {
    configs.updateOne(eq("_id", record.getConfigId()), record.toDocument(), new UpdateOptions().upsert(true));
  }
}
