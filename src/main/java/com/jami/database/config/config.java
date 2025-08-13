package com.jami.database.config;

import static com.jami.App.mongoClient;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.database.getOrDefault;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class config {
  private static MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
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

    this.configName = getOrDefault.String(entry, "_id", configName);
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

  public void setConfigName(String name) {
    this.configName = name;
  }

  public String getConfigName() {
    return configName;
  }

  public void setBotStatus(String status) {
    this.botStatus = status;
  }

  public String getBotStatus() {
    return botStatus;
  }

  public void setExpIncrement(long increment) {
    this.expIncrement = increment;
  }

  public long getExpIncrement() {
    return expIncrement;
  }

  public void setExpVariation(long variation) {
    this.expVariation = variation;
  }

  public long getExpVariation() {
    return expVariation;
  }

  public void setExpCooldown(long cooldown) {
    this.expCooldown = cooldown;
  }

  public long getExpCooldown() {
    return expCooldown;
  }

  public void setLevelBase(long levelBase) {
    this.levelBase = levelBase;
  }

  public long getLevelBase() {
    return levelBase;
  }

  public void setLevelGrowth(double growth) {
    this.levelGrowth = growth;
  }

  public double getLevelGrowth() {
    return levelGrowth;
  }

  public void addDisabledFeature(String feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(String feature) {
    this.disabledFeatures.remove(feature);
  }

  public List<String> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void addDisabledCommand(String feature) {
    this.disabledCommands.add(feature);
  }

  public void removeDisabledCommand(String feature) {
    this.disabledCommands.add(feature);
  }

  public List<String> getDisabledCommands() {
    return disabledCommands;
  }

  public void addAdminId(long id) {
    this.adminIds.add(id);
  }

  public void removeAdminId(long id) {
    this.adminIds.remove(id);
  }

  public List<Long> getAdminIds() {
    return adminIds;
  }

  public void saveConfig() {
    Document config = new Document("$set", new Document()
        .append("_id", configName)
        .append("botStatus", botStatus)
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("disabledFeatures", disabledFeatures)
        .append("disabledCommands", disabledCommands)
        .append("adminIds", adminIds));
    UpdateOptions opts = new UpdateOptions().upsert(true);
    configs.updateOne(eq("_id", configName), config, opts);
  }

}
