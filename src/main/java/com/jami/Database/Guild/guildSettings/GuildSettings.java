package com.jami.Database.Guild.guildSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.Feature;
import com.jami.Database.Enumerators.LogType;
import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingSettings;
import com.jami.Database.Guild.guildSettings.LoggingChannel.LoggingChannel;
import com.jami.Database.Guild.guildSettings.WelcomeMessageSettings.WelcomeMessageSettings;

public class GuildSettings {

  private LevellingSettings levellingSettings;
  private WelcomeMessageSettings welcomeMessageSettings;
  private List<LoggingChannel> loggingChannels = new ArrayList<>();
  private List<Feature> disabledFeatures = new ArrayList<>();
  private List<Long> wordsDisabledChannels = new ArrayList<>();

  public GuildSettings(Document doc) {
    this.levellingSettings = new LevellingSettings(GetorDefault.Document(doc, "levellingSettings"));
    this.welcomeMessageSettings = new WelcomeMessageSettings(GetorDefault.Document(doc, "welcomeMessageSettings"));
    for (Document lc : doc.getList("loggingChannels", Document.class)) {
      loggingChannels.add(new LoggingChannel(lc));
    }
    this.disabledFeatures = GetorDefault.EnumList(doc, "disabledFeatures", Feature.class, new ArrayList<>());
    this.wordsDisabledChannels = GetorDefault.List(doc, "wordsDisabledChannels", Long.class, new ArrayList<>());
  }

  public LevellingSettings getLevellingSettings() {
    return levellingSettings;
  }

  public List<LoggingChannel> getLoggingChannels() {
    return loggingChannels;
  }

  public WelcomeMessageSettings getWelcomeMessageSettings() {
    return welcomeMessageSettings;
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
    List<Document> lcs = new ArrayList<>();
    for (LoggingChannel lc : loggingChannels) {
      lcs.add(lc.toDocument());
    }
    return new Document()
        .append("levellingSettings", levellingSettings.toDocument())
        .append("welcomeMessageSettings", welcomeMessageSettings.toDocument())
        .append("loggingChannels", lcs)
        .append("disabledFeatures", EnumToString.getFromList(disabledFeatures))
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }

}
