package com.jami.Database.Guild.guildSettings.WelcomeMessage;

import org.bson.Document;

import com.jami.Database.GetOrDefault;

public class WelcomeMessageSettings {
  private WelcomeMessageType messageType;
  private String welcomeMessage;
  private long welcomeChannelId;

  public WelcomeMessageSettings(Document doc) {
    this.messageType = GetOrDefault.WelcomeMessageType(doc, "messageType", WelcomeMessageType.MESSAGE);
    this.welcomeMessage = GetOrDefault.String(doc, "welcomeMessage", "Welcome, $userMention$");
    this.welcomeChannelId = GetOrDefault.Long(doc, "welcomeChannelId", null);
  }

  public void setMessageType(WelcomeMessageType type) {
    this.messageType = type;
  }

  public WelcomeMessageType getMessageType() {
    return messageType;
  }

  public void setWelcomeMessage(String message) {
    this.welcomeMessage = message;
  }

  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  public void setWelcomeChannelId(long id) {
    this.welcomeChannelId = id;
  }

  public long getWelcomeChannelId() {
    return welcomeChannelId;
  }

  public Document toDocument() {
    return new Document()
        .append("messageType", messageType)
        .append("welcomeMessage", welcomeMessage)
        .append("welcomeChannelId", welcomeChannelId);
  }
}
