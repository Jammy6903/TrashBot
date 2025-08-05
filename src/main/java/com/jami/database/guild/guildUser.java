package com.jami.database.guild;

import org.bson.Document;

public class guildUser {
  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public guildUser(Document user, long id) {
    this.userId = id;
    this.userExp = 0;
    this.userLevel = 0;
    this.userLastMessage = 0;
    if (user != null) {
      this.userExp = user.getLong("userExp");
      this.userLevel = user.getInteger("userLevel");
      this.userLastMessage = user.getLong("userLastMessage");
    }
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

  public Document toDocument() {
    return new Document("$set", new Document()
        .append("_id", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage));
  }
}
