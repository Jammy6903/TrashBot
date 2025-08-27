package com.jami.Database.Guild.fun;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Counting {

  @BsonProperty("currentNumber")
  private long currentNumber;

  @BsonProperty("highestNumber")
  private long highestNumber;

  @BsonProperty("highestUserId")
  private long highestUserId;

  @BsonExtraElements
  private Document legacyValues;

  public Counting() {
  }

  // currentNumber

  public void setCurrentNumber(long number) {
    this.currentNumber = number;
  }

  public long getCurrentNumber() {
    return currentNumber;
  }

  // highestNumber

  public void setHighestNumber(long number) {
    this.currentNumber = number;
  }

  public long getHighestNumber() {
    return highestNumber;
  }

  // highestUserId;

  public void setHighestUserId(long id) {
    this.highestUserId = id;
  }

  public long getHighestUserId() {
    return highestUserId;
  }
}
