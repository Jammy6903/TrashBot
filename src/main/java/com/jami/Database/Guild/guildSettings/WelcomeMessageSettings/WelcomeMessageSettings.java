package com.jami.Database.Guild.guildSettings.WelcomeMessageSettings;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Enumerators.WelcomeMessageType;

public class WelcomeMessageSettings {

  @BsonProperty("messageType")
  private WelcomeMessageType messageType;

  @BsonProperty("welcomeMessage")
  private String welcomeMessage;

  @BsonProperty("welcomeChannelId")
  private long welcomeChannelId;

  @BsonExtraElements
  private Document legacyValues;

  public WelcomeMessageSettings() {
  }

  // MessageType

  public void setMessageType(WelcomeMessageType type) {
    this.messageType = type;
  }

  public WelcomeMessageType getMessageType() {
    return messageType;
  }

  // WelcomeMessage

  public void setWelcomeMessage(String message) {
    this.welcomeMessage = message;
  }

  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  // WelcomeChannel

  public void setWelcomeChannelId(long id) {
    this.welcomeChannelId = id;
  }

  public long getWelcomeChannelId() {
    return welcomeChannelId;
  }

}
