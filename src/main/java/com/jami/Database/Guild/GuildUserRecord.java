package com.jami.Database.Guild;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class GuildUserRecord {

  @BsonId
  private ObjectId recordId;

  @BsonProperty("guildId")
  private long guildId;

  @BsonProperty("userId")
  private long userId;

  @BsonProperty("userExp")
  private long userExp;

  @BsonProperty("userLevel")
  private int userLevel;

  @BsonProperty("userLastMessage")
  private long userLastMessage;

  @BsonProperty("dateCreated")
  private long dateCreated;

  public GuildUserRecord() {
  }

  // RecordId

  public ObjectId getRecordId() {
    return recordId;
  }

  // GuildId

  public long getGuildId() {
    return guildId;
  }

  // UserId

  public long getUserId() {
    return userId;
  }

  // UserExp

  public void setExp(long exp) {
    this.userExp = exp;
  }

  public long getExp() {
    return this.userExp;
  }

  // UserLevel

  public void setLevel(int level) {
    this.userLevel = level;
  }

  public int getLevel() {
    return this.userLevel;
  }

  // UserLastMessage

  public void setUserLastMessage(long time) {
    this.userLastMessage = time;
  }

  public long getUserLastMessage() {
    return userLastMessage;
  }

  // DateCreated

  public long getDateCreated() {
    return dateCreated;
  }

}
