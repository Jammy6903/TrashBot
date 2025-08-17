package com.jami.database.guild.guildSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.jami.App;
import com.jami.database.Feature;
import com.jami.database.LogType;
import com.jami.database.getOrDefault;
import com.jami.database.guild.guildSettings.loggingChannels.*;

public class guildSettings {
  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private List<loggingChannel> loggingChannels;
  private List<Integer> roleLevels;
  private List<Document> levellingRoles;
  private List<Feature> disabledFeatures;
  private List<Long> wordsDisabledChannels;

  public guildSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.expIncrement = s.getInteger("expIncrement", App.CONFIG.getExpIncrement());
    this.expVariation = s.getInteger("expVariation", App.CONFIG.getExpVariation());
    this.expCooldown = getOrDefault.Long(s, "expCooldown", App.CONFIG.getExpCooldown());
    this.levelBase = getOrDefault.Long(s, "levelBase", App.CONFIG.getLevelBase());
    this.levelGrowth = getOrDefault.Double(s, "levelGrowth", App.CONFIG.getLevelGrowth());
    this.loggingChannels = new ArrayList<>();
    for (Document doc : s.getList("loggingChannels", Document.class, new ArrayList<>())) {
      this.loggingChannels.add(new loggingChannel(doc));
    }
    this.roleLevels = s.getList("roleLevels", Integer.class, new ArrayList<>());
    this.levellingRoles = s.getList("levellingRoles", Document.class, new ArrayList<>());
    List<String> featureStrings = s.getList("disabledFeatures", String.class, new ArrayList<>());
    this.disabledFeatures = featureStrings.stream()
        .map(Feature::valueOf) // converts "MESSAGE_DELETED" -> LogType.MESSAGE_DELETED
        .collect(Collectors.toList());
    this.wordsDisabledChannels = s.getList("wordsDisabledChannels", Long.class, new ArrayList<>());
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

  public List<loggingChannel> getLoggingChannels() {
    return loggingChannels;
  }

  public loggingChannel getLoggingChannelByChannelId(long id) {
    for (loggingChannel channel : loggingChannels) {
      if (channel.getLogChannel() == id) {
        return channel;
      }
    }
    return null;
  }

  public List<loggingChannel> getLoggingChannelsByLogType(LogType type) {
    List<loggingChannel> channels = new ArrayList<>();
    for (loggingChannel channel : loggingChannels) {
      if (channel.getAssociatedLogs().contains(type)) {
        channels.add(channel);
      }
    }
    return channels;
  }

  public void addLoggingChannel(loggingChannel channel) {
    this.loggingChannels.add(channel);
  }

  public void removeLoggingChannel(loggingChannel channel) {
    this.loggingChannels.remove(channel);
  }

  public List<Integer> getRoleLevels() {
    return roleLevels;
  }

  public void addRoleLevel(int level) {
    this.roleLevels.add(level);
  }

  public void addLevellingRole(Document doc) {
    this.levellingRoles.add(doc);
  }

  public List<Document> getLevellingRoles() {
    return levellingRoles;
  }

  public List<Feature> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void addDisabledFeature(Feature feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(Feature feature) {
    this.disabledFeatures.remove(feature);
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
    List<Document> loggingChannelsDocs = new ArrayList<>();
    for (loggingChannel channel : loggingChannels) {
      loggingChannelsDocs.add(channel.toDocument());
    }
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("loggingChannels", loggingChannelsDocs)
        .append("roleLevels", roleLevels)
        .append("levellingRoles", levellingRoles)
        .append("disabledFeatures", disabledFeatures)
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }
}
