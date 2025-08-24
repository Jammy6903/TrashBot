package com.jami.Database.Guild.guildSettings.WelcomeMessageSettings;

import org.bson.Document;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.WelcomeMessageType;

public class WelcomeMessageSettings {
  private WelcomeMessageType messageType;
  private String welcomeMessage;
  private long welcomeChannelId;

  public WelcomeMessageSettings(Document doc) {
    this.messageType = GetorDefault.Enum(doc, "messageType", WelcomeMessageType.class, WelcomeMessageType.TEXT);
    this.welcomeMessage = GetorDefault.String(doc, "welcomeMessage", "Welcome $userMention$");
    this.welcomeChannelId = GetorDefault.Long(doc, "welcomeChannelId", 0L);
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
        .append("messageType", EnumToString.get(messageType))
        .append("welcomeMessage", welcomeMessage)
        .append("welcomeChannelId", welcomeChannelId);
  }

}
