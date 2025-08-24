package com.jami.Database.Config;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.Database.GetorDefault;
import com.jami.Database.Config.BotColors.BotColors;
import com.jami.Database.Config.LevellingConfig.LevellingConfig;
import com.jami.Database.Enumerators.Command;
import com.jami.Database.Enumerators.Feature;

public class ConfigRecord {
  private ObjectId configId;
  private String configName;
  private String botStatus;
  private BotColors botColors;
  private LevellingConfig levellingConfig;
  private List<Feature> disabledFeatures;
  private List<Command> disabledCommands;
  private List<Long> adminIds;

  private static String defaultBotStatus = "Bottin'";
  private static List<Feature> defaultDisabledFeatures = new ArrayList<>();
  private static List<Command> defaultDisabledCommands = new ArrayList<>();
  private static List<Long> defaultAdminIds = new ArrayList<>();

  public ConfigRecord(Document doc) {
    this.configId = doc.getObjectId("_id");
    this.configName = doc.getString("configName");
    this.botStatus = GetorDefault.String(doc, "botStatus", defaultBotStatus);
    this.botColors = new BotColors(doc.get("botColors", Document.class));
    this.levellingConfig = new LevellingConfig(doc.get("levellingConfig", Document.class));
    this.disabledFeatures = GetorDefault.EnumList(doc, "disabledFeatures", Feature.class, defaultDisabledFeatures);
    this.disabledCommands = GetorDefault.EnumList(doc, "disabledCommands", Command.class, defaultDisabledCommands);
    this.adminIds = doc.getList("adminIds", Long.class, defaultAdminIds);
  }

  public ConfigRecord(String configName) {
    this.configId = new ObjectId();
    this.configName = configName;
    this.botStatus = defaultBotStatus;
    this.botColors = new BotColors(new Document());
    this.levellingConfig = new LevellingConfig(new Document());
    this.disabledFeatures = defaultDisabledFeatures;
    this.disabledCommands = defaultDisabledCommands;
    this.adminIds = defaultAdminIds;
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

  public LevellingConfig getLevellingConfig() {
    return levellingConfig;
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

  public void addDisabledCommand(Command feature) {
    this.disabledCommands.add(feature);
  }

  public void removeDisabledCommand(Command feature) {
    this.disabledCommands.add(feature);
  }

  public List<Command> getDisabledCommands() {
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

  public Document toDocument() {
    return new Document()
        .append("configId", configId)
        .append("configName", configName)
        .append("botStatus", botStatus)
        .append("botColors", botColors.toDocument())
        .append("levellingConfig", levellingConfig.toDocument())
        .append("disabledFeatures", disabledFeatures)
        .append("disabledCommands", disabledCommands)
        .append("adminIds", adminIds);
  }

}
