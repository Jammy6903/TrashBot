package com.jami.database.guild.guildSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.App;
import com.jami.database.getOrDefault;

public class guildSettings {
  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private List<Document> loggingChannels;
  private List<Integer> roleLevels;
  private List<Document> levellingRoles;
  private List<String> disabledFeatures;
  private List<Long> wordsDisabledChannels;

  private static List<Document> defaultLoggingChannels = new ArrayList<>();
  private static List<Integer> defaultRoleLevels = new ArrayList<>();
  private static List<Document> defaultLevellingRoles = new ArrayList<>();
  private static List<String> defaultEnabledFeatures = new ArrayList<>();
  private static List<Long> defaultWordsDisabledChannels = new ArrayList<>();

  public guildSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.expIncrement = s.getInteger("expIncrement", App.CONFIG.getExpIncrement());
    this.expVariation = s.getInteger("expVariation", App.CONFIG.getExpVariation());
    this.expCooldown = getOrDefault.Long(s, "expCooldown", App.CONFIG.getExpCooldown());
    this.levelBase = getOrDefault.Long(s, "levelBase", App.CONFIG.getLevelBase());
    this.levelGrowth = getOrDefault.Double(s, "levelGrowth", App.CONFIG.getLevelGrowth());
    this.loggingChannels = s.getList("loggingChannels", Document.class, defaultLoggingChannels);
    this.roleLevels = s.getList("roleLevels", Integer.class, defaultRoleLevels);
    this.levellingRoles = s.getList("levellingRoles", Document.class, defaultLevellingRoles);
    this.disabledFeatures = s.getList("disabledFeatures", String.class, defaultEnabledFeatures);
    this.wordsDisabledChannels = s.getList("wordsDisabledChannels", Long.class, defaultWordsDisabledChannels);
  }

  public void setExpIncrement(int exp) {
    this.expIncrement = exp;
  }

  public int getExpIncrement() {
    return expIncrement;
  }

  public void setExpVariation(int variation) {
    this.expVariation = variation;
  }

  public int getExpVariation() {
    return expVariation;
  }

  public void setExpCooldown(long cooldown) {
    this.expCooldown = cooldown;
  }

  public long getExpCooldown() {
    return expCooldown;
  }

  public void setLevelBase(long base) {
    this.levelBase = base;
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

  public void setLoggingChannels(List<Document> channels) {
    this.loggingChannels = channels;
  }

  public List<Document> getLoggingChannels() {
    return loggingChannels;
  }

  public void addLoggingChannel(Document channel) {
    this.loggingChannels.add(channel);
  }

  public void removeLoggingChannel(Document channel) {
    this.loggingChannels.remove(channel);
  }

  public void setRoleLevels(List<Integer> levels) {
    this.roleLevels = levels;
  }

  public List<Integer> getRoleLevels() {
    return roleLevels;
  }

  public void addRoleLevel(int level) {
    this.roleLevels.add(level);
  }

  public void setLevellingRoles(List<Document> docs) {
    this.levellingRoles = docs;
  }

  public void addLevellingRole(Document doc) {
    this.levellingRoles.add(doc);
  }

  public List<Document> getLevellingRoles() {
    return levellingRoles;
  }

  public void setDisabledFeatures(List<String> features) {
    this.disabledFeatures = features;
  }

  public List<String> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void addDisabledFeature(String feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(String feature) {
    this.disabledFeatures.remove(feature);
  }

  public void setWordsDisabledChannels(List<Long> channels) {
    this.wordsDisabledChannels = channels;
  }

  public List<Long> getWordsDisabledChannels() {
    return wordsDisabledChannels;
  }

  public void addWordsDisabledChannel(long channelId) {
    this.wordsDisabledChannels.add(channelId);
  }

  public void removeWordsDisabledChannel(long channelId) {
    this.wordsDisabledChannels.remove(channelId);
  }

  public Document toDocument() {
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("roleLevels", roleLevels)
        .append("levellingRoles", levellingRoles)
        .append("disabledFeatures", disabledFeatures)
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }
}
