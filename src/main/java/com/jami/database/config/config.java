package com.jami.database.config;

import static com.jami.App.mongoClient;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.database.getOrDefault;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class config {
  private static MongoDatabase db = mongoClient.getDatabase("BOT");
  private static MongoCollection<Document> configs = db.getCollection("configs");

  private String configName;

  private String botStatus;

  private long expIncrement;
  private long expVariation;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private List<String> disabledFeatures;
  private List<String> disabledCommands;

  private List<Long> adminIds;

  public config(String configName) {
    Document entry = configs.find(eq("_id", configName)).first();
    if (entry == null) {
      entry = new Document();
    }

    this.configName = configName;
    this.botStatus = getOrDefault.String(entry, "botStatus", "");
    this.expIncrement = getOrDefault.Long(entry, "expIncrement", 0L);
    this.expVariation = getOrDefault.Long(entry, "expVariation", 0L);
    this.expCooldown = getOrDefault.Long(entry, "expCooldown", 0L);
    this.levelBase = getOrDefault.Long(entry, "levelBase", 0L);
    this.levelGrowth = getOrDefault.Double(entry, "levelGrowth", 0.0);
    this.disabledFeatures = entry.getList("disabledFeatures", String.class, new ArrayList<>());
    this.disabledCommands = entry.getList("disabledCommands", String.class, new ArrayList<>());
    this.adminIds = entry.getList("adminIds", Long.class, new ArrayList<>());
  }

}
