package com.jami.database.guild;

import org.bson.Document;

public class guildUser {

  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public guildUser(Document u, long id) {
    if (u != null) {
      this.userId = id;
      this.userExp = u.getLong("userExp");
      this.userLevel = u.getInteger("userLevel");
      this.userLastMessage = u.getLong("userLastMessage");
    } else {
      this.userId = id;
      this.userExp = 0;
      this.userLevel = 0;
      this.userLastMessage = 0;
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
    return new Document()
        .append("_id", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage);
  }
}
