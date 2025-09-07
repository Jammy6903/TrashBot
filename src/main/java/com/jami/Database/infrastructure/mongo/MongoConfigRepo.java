package com.jami.Database.infrastructure.mongo;

import static com.mongodb.client.model.Filters.eq;

import org.bson.types.ObjectId;

import com.jami.Database.Config.ConfigRecord;
import com.jami.Database.repository.ConfigRepo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;

public class MongoConfigRepo implements ConfigRepo {
  private MongoCollection<ConfigRecord> configs;

  public MongoConfigRepo() {
    this.configs = Mongo.getDatabase().getCollection("CONFIGS", ConfigRecord.class);
  }

  @Override
  public ConfigRecord getById(String id) {
    return configs.find(eq("_id", new ObjectId(id))).first();
  }

  @Override
  public ConfigRecord getByName(String name) {
    return configs.find(eq("configName", name)).first();
  }

  @Override
  public void create(String name) {
    configs.insertOne(new ConfigRecord(name));
  }

  @Override
  public void save(ConfigRecord record) {
    configs.replaceOne(eq("_id", record.getConfigId()), record, new ReplaceOptions().upsert(true));
  }
}
