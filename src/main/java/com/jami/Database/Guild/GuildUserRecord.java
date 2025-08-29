package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
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
  private long userExp = 0;

  @BsonProperty("userLevel")
  private int userLevel = 0;

  @BsonProperty("countingHighestNumber")
  private long countingHighestNumber = 0;

  @BsonProperty("userLastMessage")
  private long userLastMessage = 0;

  @BsonProperty("dateCreated")
  private long dateCreated;

  @BsonExtraElements
  private Document legacyValues;

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

  // countingHighestNumber

  public void setCountingHighestNumber(long number) {
    this.countingHighestNumber = number;
  }

  public long getCountingHighestNumber() {
    return countingHighestNumber;
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

  public void setDateCreated(long dateCreated) {
    this.dateCreated = dateCreated;
  }

}
