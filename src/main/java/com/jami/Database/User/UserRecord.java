package com.jami.Database.User;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UserRecord {

  @BsonId
  private long userId;

  @BsonProperty("userExp")
  private long userExp;

  @BsonProperty("userLevel")
  private int userLevel;

  @BsonProperty("userLastMessage")
  private long userLastMessage;

  @BsonProperty("userSettings")
  private UserSettings userSettings = new UserSettings();

  @BsonProperty("dateCreated")
  private long dateCreated;

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

  // Date

  public long getDateCreated() {
    return dateCreated;
  }

}
