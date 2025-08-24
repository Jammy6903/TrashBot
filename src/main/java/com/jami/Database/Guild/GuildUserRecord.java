package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.types.ObjectId;

public class GuildUserRecord {
  private ObjectId recordId;
  private long guildId;
  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public GuildUserRecord(Document doc) {
    this.recordId = doc.getObjectId("_id");
    this.guildId = doc.getLong("guildId");
    this.userId = doc.getLong("userId");
    this.userExp = doc.getLong("userExp");
    this.userLevel = doc.getInteger("userLevel");
    this.userLastMessage = doc.getLong("userLastMessage");
  }

  public GuildUserRecord(long guildId, long userId) {
    this.recordId = new ObjectId();
    this.guildId = guildId;
    this.userId = userId;
    this.userExp = 0;
    this.userLevel = 0;
    this.userLastMessage = System.currentTimeMillis();
  }

  public void newRecordId() {
    recordId = new ObjectId();
  }

  public String getRecordId() {
    return recordId.toString();
  }

  public void setGuildId(long guildId) {
    this.guildId = guildId;
  }

  public long getGuildId() {
    return guildId;
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
    return this.userExp;
  }

  public void setLevel(int level) {
    this.userLevel = level;
  }

  public int getLevel() {
    return this.userLevel;
  }

  public void setUserLastMessage(long time) {
    this.userLastMessage = time;
  }

  public long getUserLastMessage() {
    return userLastMessage;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", recordId)
        .append("guildId", guildId)
        .append("userId", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage);
  }
}
