package com.jami.Database.Guild.guildSettings.WelcomeMessageSettings;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Enumerators.WelcomeMessageType;

public class WelcomeMessageSettings {

  @BsonProperty("messageType")
  private WelcomeMessageType messageType;

  @BsonProperty("welcomeMessage")
  private String welcomeMessage;

  @BsonProperty("welcomeChannelId")
  private long welcomeChannelId;

  public WelcomeMessageSettings() {
  }

  public WelcomeMessageSettings(boolean d) {
    this.messageType = WelcomeMessageType.TEXT;
    this.welcomeMessage = "Welcome $userMention$!";
    this.welcomeChannelId = 0L;
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
