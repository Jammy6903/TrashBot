package com.jami.Database.Config;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.jami.Database.Config.BotColors.BotColors;
import com.jami.Database.Config.LevellingConfig.LevellingConfig;
import com.jami.Database.Enumerators.Command;
import com.jami.Database.Enumerators.Feature;

public class ConfigRecord {

  @BsonId
  private ObjectId configId;

  @BsonProperty("configName")
  private String configName;

  @BsonProperty("botStatus")
  private String botStatus;

  @BsonProperty("botColors")
  private BotColors botColors = new BotColors();

  @BsonProperty("levellingConfig")
  private LevellingConfig levellingConfig = new LevellingConfig();

  @BsonProperty("disabledFeatures")
  private List<Feature> disabledFeatures = new ArrayList<>();

  @BsonProperty("disabledCommands")
  private List<Command> disabledCommands = new ArrayList<>();

  @BsonProperty("adminIds")
  private List<Long> adminIds = new ArrayList<>();

  @BsonExtraElements
  private Document legacyValues;

  public ConfigRecord() {
  }

  public ConfigRecord(String configName) {
    this.configId = new ObjectId();
    this.configName = configName;
    this.botStatus = "bottin'";
    this.botColors = new BotColors();
    this.levellingConfig = new LevellingConfig();
    this.disabledFeatures = new ArrayList<>();
    this.disabledCommands = new ArrayList<>();
    this.adminIds = new ArrayList<>();
  }

  public void newConfigId() {
    this.configId = new ObjectId();
  }

  public String getConfigId() {
    return configId.toString();
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

  public BotColors getBotColors() {
    return botColors;
  }

  public void setBotColors(BotColors botColors) {
    this.botColors = botColors;
  }

  public LevellingConfig getLevellingConfig() {
    return levellingConfig;
  }

  public void setLevellingConfig(LevellingConfig levellingConfig) {
    this.levellingConfig = levellingConfig;
  }

  public void addDisabledFeature(Feature feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(Feature feature) {
    this.disabledFeatures.remove(feature);
  }

  public List<Feature> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void setDisabledFeatures(List<Feature> disabledFeatures) {
    this.disabledFeatures = disabledFeatures;
  }

  public void addDisabledCommand(Command feature) {
    this.disabledCommands.add(feature);
  }

  public void removeDisabledCommand(Command feature) {
    this.disabledCommands.remove(feature);
  }

  public List<Command> getDisabledCommands() {
    return disabledCommands;
  }

  public void setDisabledCommands(List<Command> disabledCommands) {
    this.disabledCommands = disabledCommands;
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

  public void setAdminIds(List<Long> adminIds) {
    this.adminIds = adminIds;
  }

}
