package com.jami.Database.User;

import org.bson.Document;

public class UserRecord {
  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;
  private UserSettings userSettings;

  public UserRecord(Document doc) {
    this.userId = doc.getLong("_id");
    this.userExp = doc.getLong("userExp");
    this.userLevel = doc.getInteger("userLevel");
    this.userLastMessage = doc.getLong("userLastMessage");
    this.userSettings = new UserSettings(doc.get("userSettings", Document.class));
  }

  public UserRecord(long userId) {
    this.userId = userId;
    this.userExp = 0;
    this.userLevel = 0;
    this.userLastMessage = System.currentTimeMillis();
    this.userSettings = new UserSettings(new Document());
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getUserId() {
    return userId;
  }

  public void setExp(long exp) {
    this.userExp = exp;
  }

  public long getExp() {
    return userExp;
  }

  public void setLevel(int level) {
    this.userLevel = level;
  }

  public int getLevel() {
    return userLevel;
  }

  public void setLastMessage(long time) {
    this.userLastMessage = time;
  }

  public long getLastMessage() {
    return userLastMessage;
  }

  public UserSettings getSettings() {
    return userSettings;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage)
        .append("settings", userSettings.toDocument());
  }
}
