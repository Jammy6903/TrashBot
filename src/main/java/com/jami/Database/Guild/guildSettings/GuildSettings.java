package com.jami.Database.Guild.guildSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Enumerators.Feature;
import com.jami.Database.Enumerators.LogType;
import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingSettings;
import com.jami.Database.Guild.guildSettings.LoggingChannel.LoggingChannel;
import com.jami.Database.Guild.guildSettings.WelcomeMessageSettings.WelcomeMessageSettings;
import com.jami.Database.Guild.guildSettings.countingSettings.CountingSettings;

public class GuildSettings {

  @BsonProperty("levellingSettings")
  private LevellingSettings levellingSettings = new LevellingSettings();

  @BsonProperty("countingSettings")
  private CountingSettings countingSettings = new CountingSettings();

  @BsonProperty("welcomeMessageSettings")
  private WelcomeMessageSettings welcomeMessageSettings = new WelcomeMessageSettings();

  @BsonProperty("loggingChannels")
  private List<LoggingChannel> loggingChannels = new ArrayList<>();

  @BsonProperty("disabledFeatures")
  private List<Feature> disabledFeatures = new ArrayList<>();

  @BsonProperty("wordsDisabledChannels")
  private List<Long> wordsDisabledChannels = new ArrayList<>();

  @BsonExtraElements
  private Document legacyValues;

  public GuildSettings() {
  }

  // LevellingSettings

  public LevellingSettings getLevellingSettings() {
    return levellingSettings;
  }

  public void setLevellingSettings(LevellingSettings levellingSettings) {
    this.levellingSettings = levellingSettings;
  }

  // CountingSettings

  public CountingSettings getCountingSettings() {
    return countingSettings;
  }

  public void setCountingSettings(CountingSettings countingSettings) {
    this.countingSettings = countingSettings;
  }

  // LoggingChannel

  public void addLoggingChannel(LoggingChannel channel) {
    this.loggingChannels.add(channel);
  }

  public void removeLoggingChannel(LoggingChannel channel) {
    this.loggingChannels.remove(channel);
  }

  public List<LoggingChannel> getLoggingChannels() {
    return loggingChannels;
  }

  public void setLoggingChannels(List<LoggingChannel> loggingChannels) {
    this.loggingChannels = loggingChannels;
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

  // WelcomeMessageSettings

  public WelcomeMessageSettings getWelcomeMessageSettings() {
    return welcomeMessageSettings;
  }

  public void setWelcomeMessageSettings(WelcomeMessageSettings welcomeMessageSettings) {
    this.welcomeMessageSettings = welcomeMessageSettings;
  }

  // DisabledFeatures

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

  // WordsDisabledChannels

  public void addWordsDisabledChannel(long channelId) {
    this.wordsDisabledChannels.add(channelId);
  }

  public void removeWordsDisabledChannel(long channelId) {
    this.wordsDisabledChannels.remove(channelId);
  }

  public List<Long> getWordsDisabledChannels() {
    return wordsDisabledChannels;
  }

  public void setWordsDisabledChannels(List<Long> wordsDisabledChannels) {
    this.wordsDisabledChannels = wordsDisabledChannels;
  }
}
