package com.jami.Database.Guild.guildSettings.LoggingChannel;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Enumerators.LogType;

public class LoggingChannel {

  @BsonProperty("logChannelId")
  private long logChannelId;

  @BsonProperty("associatedLogs")
  private List<LogType> associatedLogs = new ArrayList<>();

  @BsonExtraElements
  private Document legacyValues;

  public LoggingChannel() {
  }

  // LoggingChannel

  public void setLogChannelId(long id) {
    this.logChannelId = id;
  }

  public long getLogChannel() {
    return logChannelId;
  }

  // AssociatedLogs

  public void addAssociatedLog(LogType type) {
    this.associatedLogs.add(type);
  }

  public void addAssociatedLogs(List<LogType> types) {
    this.associatedLogs.addAll(types);
  }

  public void removeAssociatedLog(LogType type) {
    this.associatedLogs.remove(type);
  }

  public List<LogType> getAssociatedLogs() {
    return associatedLogs;
  }

  public void setAssociatedLogs(List<LogType> associatedLogs) {
    this.associatedLogs = associatedLogs;
  }

}
