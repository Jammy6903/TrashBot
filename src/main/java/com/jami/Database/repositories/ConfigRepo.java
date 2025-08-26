package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Config.ConfigRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;

public class ConfigRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<ConfigRecord> configs = database.getCollection("CONFIGS", ConfigRecord.class);

  public static ConfigRecord getById(String id) {
    return configs.find(eq("_id", new ObjectId(id))).first();
  }

  public static ConfigRecord getByName(String name) {
    return configs.find(eq("configName", name)).first();
  }

  public static void create(String name) {
    configs.insertOne(new ConfigRecord(name));
  }

  public static void save(ConfigRecord record) {
    configs.replaceOne(eq("_id", record.getConfigId()), record, new ReplaceOptions().upsert(true));
  }
}
