package com.jami.Database.Guild.guildSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.jami.App;
import com.jami.Database.Feature;
import com.jami.Database.LogType;
import com.jami.Database.GetOrDefault;
import com.jami.Database.Guild.guildSettings.LevellingRoles.LevellingRole;
import com.jami.Database.Guild.guildSettings.LoggingChannels.*;
import com.jami.Database.Guild.guildSettings.WelcomeMessage.WelcomeMessageSettings;

public class GuildSettings {
  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private WelcomeMessageSettings welcomeMessage;
  private List<LoggingChannel> loggingChannels;
  private List<LevellingRole> levellingRoles;
  private List<Feature> disabledFeatures;
  private List<Long> wordsDisabledChannels;

  public GuildSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.expIncrement = s.getInteger("expIncrement", App.getGlobalConfig().getExpIncrement());
    this.expVariation = s.getInteger("expVariation", App.getGlobalConfig().getExpVariation());
    this.expCooldown = GetOrDefault.Long(s, "expCooldown", App.getGlobalConfig().getExpCooldown());
    this.levelBase = GetOrDefault.Long(s, "levelBase", App.getGlobalConfig().getLevelBase());
    this.levelGrowth = GetOrDefault.Double(s, "levelGrowth", App.getGlobalConfig().getLevelGrowth());
    this.welcomeMessage = new WelcomeMessageSettings(s);
    this.loggingChannels = new ArrayList<>();
    for (Document doc : s.getList("loggingChannels", Document.class, new ArrayList<>())) {
      this.loggingChannels.add(new LoggingChannel(doc));
    }
    this.levellingRoles = new ArrayList<>();
    for (Document doc : s.getList("levellingRoles", Document.class, new ArrayList<>())) {
      this.levellingRoles.add(new LevellingRole(doc));
    }
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

  public List<LoggingChannel> getLoggingChannels() {
    return loggingChannels;
  }

  public WelcomeMessageSettings getWelcomeMessageSettings() {
    return welcomeMessage;
  }

  public LoggingChannel getLoggingChannelByChannelId(long id) {
    for (LoggingChannel channel : loggingChannels) {
      if (channel.getLogChannel() == id) {
        return channel;
      }
    }
    return null;
  }

  public List<LoggingChannel> getLoggingChannelsByLogType(LogType type) {
    List<LoggingChannel> channels = new ArrayList<>();
    for (LoggingChannel channel : loggingChannels) {
      if (channel.getAssociatedLogs().contains(type)) {
        channels.add(channel);
      }
    }
    return channels;
  }

  public void addLoggingChannel(LoggingChannel channel) {
    this.loggingChannels.add(channel);
  }

  public void removeLoggingChannel(LoggingChannel channel) {
    this.loggingChannels.remove(channel);
  }

  public LevellingRole getLevelRoleByRoleId(long id) {
    for (LevellingRole role : levellingRoles) {
      if (role.getId() == id) {
        return role;
      }
    }
    return null;
  }

  public List<LevellingRole> getLevelRolesByLevel(int level) {
    List<LevellingRole> roles = new ArrayList<>();
    for (LevellingRole role : levellingRoles) {
      if (role.getLevelRequirement() == level) {
        roles.add(role);
      }
    }
    return roles;
  }

  public List<LevellingRole> getLevellingRoles() {
    return levellingRoles;
  }

  public void addLevellingRole(LevellingRole role) {
    this.levellingRoles.add(role);
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
    for (LoggingChannel channel : loggingChannels) {
      loggingChannelsDocs.add(channel.toDocument());
    }
    List<Document> levellingRolesDocs = new ArrayList<>();
    for (LevellingRole role : levellingRoles) {
      levellingRolesDocs.add(role.toDocument());
    }
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("welcomeMessage", welcomeMessage.toDocument())
        .append("loggingChannels", loggingChannelsDocs)
        .append("levellingRoles", levellingRolesDocs)
        .append("disabledFeatures", disabledFeatures)
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }
}
