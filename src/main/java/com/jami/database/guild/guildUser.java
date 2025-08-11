package com.jami.database.guild;

import org.bson.Document;

import com.jami.database.getOrDefault;

public class guildUser {
  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public guildUser(Document user, long id) {
    if (user == null) {
      user = new Document();
    }
    this.userId = id;
    this.userExp = getOrDefault.Long(user, "userExp", 0L);
    this.userLevel = user.getInteger("userLevel", 0);
    this.userLastMessage = getOrDefault.Long(user, "userLastMessage", 0L);
  }

  public long getId() {
    return userId;
  }

  public void setId(long id) {
    this.userId = id;
  }

  public long getExp() {
    return this.userExp;
  }

  public void setExp(long exp) {
    this.userExp = exp;
  }

  public int getLevel() {
    return this.userLevel;
  }

  public void setLevel(int level) {
    this.userLevel = level;
  }

  public long getUserLastMessage() {
    return userLastMessage;
  }

  public void setUserLastMessage(long time) {
    this.userLastMessage = time;
  }

  public void incrementLevel() {
    this.userLevel++;
  }

  public Document toDocument() {
    return new Document("$set", new Document()
        .append("_id", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage));
  }
}
