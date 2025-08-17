package com.jami.database.guild.guildSettings.loggingChannels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.jami.database.LogType;

public class loggingChannel {
  private long logChannel;
  private List<LogType> associatedLogs;

  public loggingChannel(long id) {
    this.logChannel = id;
  }

  public loggingChannel(long id, List<LogType> type) {
    this.logChannel = id;
    this.associatedLogs = new ArrayList<>();
    this.associatedLogs.addAll(type);
  }

  public loggingChannel(Document doc) {
    this.logChannel = doc.getLong("logChannel");

    List<String> logStrings = doc.getList("associatedLogs", String.class, new ArrayList<>());
    this.associatedLogs = logStrings.stream()
        .map(LogType::valueOf) // converts "MESSAGE_DELETED" -> LogType.MESSAGE_DELETED
        .collect(Collectors.toList());
  }

  public long getLogChannel() {
    return logChannel;
  }

  public void addAssociatedLog(LogType type) {
    this.associatedLogs.add(type);
  }

  public void removeAssociatedLog(LogType type) {
    this.associatedLogs.remove(type);
  }

  public List<LogType> getAssociatedLogs() {
    return associatedLogs;
  }

  public Document toDocument() {
    return new Document()
        .append("logChannel", logChannel)
        .append("associatedLogs", associatedLogs);
  }
}
