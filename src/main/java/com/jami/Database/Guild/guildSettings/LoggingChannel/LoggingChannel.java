package com.jami.Database.Guild.guildSettings.LoggingChannel;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.LogType;

public class LoggingChannel {
  private long logChannelId;
  private List<LogType> associatedLogs = new ArrayList<>();

  public LoggingChannel(Document doc) {
    this.logChannelId = doc.getLong("logChannelId");
    this.associatedLogs = GetorDefault.EnumList(doc, "associatedLogs", LogType.class, new ArrayList<>());
  }

  public LoggingChannel(long channelId) {
    this.logChannelId = channelId;
  }

  public void setLogChannelId(long id) {
    this.logChannelId = id;
  }

  public long getLogChannel() {
    return logChannelId;
  }

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

  public Document toDocument() {
    return new Document()
        .append("logChannelId", logChannelId)
        .append("associatedLogs", EnumToString.getFromList(associatedLogs));
  }

}
