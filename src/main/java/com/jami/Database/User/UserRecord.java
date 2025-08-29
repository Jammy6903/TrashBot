package com.jami.Database.User;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UserRecord {

  @BsonId
  private long userId;

  @BsonProperty("userExp")
  private long userExp = 0;

  @BsonProperty("userLevel")
  private int userLevel = 0;

  @BsonProperty("userLastMessage")
  private long userLastMessage = 0;

  @BsonProperty("userSettings")
  private UserSettings userSettings = new UserSettings();

  @BsonProperty("dateCreated")
  private long dateCreated;

  @BsonExtraElements
  private Document legacyValues;

  public UserRecord() {
  }

  // UserId

  public long getUserId() {
    return userId;
  }

  // Experience

  public void setUserExp(long exp) {
    this.userExp = exp;
  }

  public long getUserExp() {
    return userExp;
  }

  // Level

  public void setUserLevel(int level) {
    this.userLevel = level;
  }

  public int getUserLevel() {
    return userLevel;
  }

  // LastMessage

  public void setUserLastMessage(long time) {
    this.userLastMessage = time;
  }

  public long getUserLastMessage() {
    return userLastMessage;
  }

  // Settings

  public UserSettings getSettings() {
    return userSettings;
  }

  public void setSettings(UserSettings userSettings) {
    this.userSettings = userSettings;
  }

  // Date

  public long getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(long dateCreated) {
    this.dateCreated = dateCreated;
  }

}
